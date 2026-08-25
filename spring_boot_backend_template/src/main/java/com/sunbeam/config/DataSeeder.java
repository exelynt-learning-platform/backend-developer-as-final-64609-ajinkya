package com.sunbeam.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sunbeam.entity.Reservation;
import com.sunbeam.entity.ReservationStatus;
import com.sunbeam.entity.Resource;
import com.sunbeam.entity.Role;
import com.sunbeam.entity.User;
import com.sunbeam.repository.ReservationRepository;
import com.sunbeam.repository.ResourceRepository;
import com.sunbeam.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      ResourceRepository resourceRepository,
                      ReservationRepository reservationRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
        this.reservationRepository = reservationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(Role.USER);
            userRepository.save(user);
        }

        if (resourceRepository.count() == 0) {
            Resource resource1 = new Resource();
            resource1.setName("Conference Room A");
            resource1.setType("Meeting Room");
            resource1.setLocation("Floor 1");
            resource1.setCapacity(12);
            resource1.setPrice(new BigDecimal("150.00"));
            resource1.setDescription("Premium conference room with projector");
            resourceRepository.save(resource1);

            Resource resource2 = new Resource();
            resource2.setName("Laptop 14");
            resource2.setType("Device");
            resource2.setLocation("IT Desk");
            resource2.setCapacity(1);
            resource2.setPrice(new BigDecimal("70.00"));
            resource2.setDescription("Business laptop rental");
            resourceRepository.save(resource2);
        }

        if (reservationRepository.count() == 0) {
            User admin = userRepository.findByUsername("admin").orElseThrow();
            User regularUser = userRepository.findByUsername("user").orElseThrow();
            Resource resource = resourceRepository.findAll().get(0);

            Reservation reservation = new Reservation();
            reservation.setUser(regularUser);
            reservation.setResource(resource);
            reservation.setStartTime(LocalDateTime.now().plusDays(1));
            reservation.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
            reservation.setStatus(ReservationStatus.PENDING);
            reservation.setPrice(resource.getPrice());
            reservation.setCreatedAt(LocalDateTime.now());
            reservation.setUpdatedAt(LocalDateTime.now());
            reservationRepository.save(reservation);
        }
    }
}
