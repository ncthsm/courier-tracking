package com.migros.couriertracking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourierDistanceResponse {
    private Long courierId;
    private Double totalDistance;
} 