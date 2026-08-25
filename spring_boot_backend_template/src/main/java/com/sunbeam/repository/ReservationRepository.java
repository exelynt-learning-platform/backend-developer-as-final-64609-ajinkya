package com.sunbeam.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sunbeam.entity.Reservation;
import com.sunbeam.entity.ReservationStatus;
import com.sunbeam.entity.User;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE (:status IS NULL OR r.status = :status) " +
           "AND (:minPrice IS NULL OR r.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR r.price <= :maxPrice) " +
           "AND (:user IS NULL OR r.user = :user)")
    Page<Reservation> findFiltered(
            @Param("status") ReservationStatus status,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("user") User user,
            Pageable pageable);

    List<Reservation> findByUser(User user);
    Page<Reservation> findByUser(User user, Pageable pageable);
}
