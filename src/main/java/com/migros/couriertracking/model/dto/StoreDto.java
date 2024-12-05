package com.migros.couriertracking.model.dto;

import lombok.Data;

@Data
public class StoreDto {
    private Long id;
    private String name;
    private double latitude;
    private double longitude;
}
