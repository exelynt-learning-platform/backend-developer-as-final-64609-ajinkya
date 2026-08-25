package com.sunbeam.dto;

import java.math.BigDecimal;

import com.sunbeam.entity.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationFilter {
    private ReservationStatus status;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String sortBy = "createdAt";
    private String sortDir = "desc";
}
