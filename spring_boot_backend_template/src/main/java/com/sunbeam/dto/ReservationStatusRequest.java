package com.sunbeam.dto;

import com.sunbeam.entity.ReservationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationStatusRequest {

    @NotNull(message = "Reservation status is required")
    private ReservationStatus status;
}
