package com.migros.couriertracking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierDto {
    private Long id;
    private String name;
    private String identityNumber;
    private Double totalTravelDistance;
} 