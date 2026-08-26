package com.sunbeam.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dto.ReservationDto;
import com.sunbeam.entity.Reservation;
import com.sunbeam.entity.ReservationStatus;
import com.sunbeam.entity.Resource;
import com.sunbeam.entity.User;
import com.sunbeam.exception.ResourceNotFoundException;
import com.sunbeam.repository.ReservationRepository;
import com.sunbeam.repository.ResourceRepository;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ResourceRepository resourceRepository;
    private final UserService userService;

    public ReservationService(ReservationRepository reservationRepository, ResourceRepository resourceRepository, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.resourceRepository = resourceRepository;
        this.userService = userService;
    }

    public Page<Reservation> getAllReservations(ReservationStatus status, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return reservationRepository.findFiltered(status, minPrice, maxPrice, null, pageable);
    }

    public Page<Reservation> getMyReservations(User user, ReservationStatus status, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return reservationRepository.findFiltered(status, minPrice, maxPrice, user, pageable);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
    }

    public Reservation createReservation(ReservationDto dto, User currentUser) {
        if (dto.getEndTime().isBefore(dto.getStartTime()) || dto.getEndTime().isEqual(dto.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        Resource resource = resourceRepository.findById(dto.getResourceId())
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + dto.getResourceId()));

        if (reservationRepository.existsOverlappingReservation(
            resource.getId(), dto.getStartTime(), dto.getEndTime())) {
            throw new IllegalArgumentException("Resource is already reserved for the requested time");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(currentUser);
        reservation.setResource(resource);
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setPrice(resource.getPrice());
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }

    public Reservation updateReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = getReservationById(id);
        reservation.setStatus(status);
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, ReservationDto dto) {
        if (dto.getEndTime().isBefore(dto.getStartTime()) || dto.getEndTime().isEqual(dto.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        Reservation reservation = getReservationById(id);
        Resource resource = resourceRepository.findById(dto.getResourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + dto.getResourceId()));

        if (reservationRepository.existsOverlappingReservationExcludingId(
                id, resource.getId(), dto.getStartTime(), dto.getEndTime())) {
            throw new IllegalArgumentException("Resource is already reserved for the requested time");
        }

        reservation.setResource(resource);
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setPrice(resource.getPrice());
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(getReservationById(id));
    }
}
