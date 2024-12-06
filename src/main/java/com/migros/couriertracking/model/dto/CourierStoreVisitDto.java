package com.migros.couriertracking.model.dto;

import com.migros.couriertracking.entity.Courier;
import com.migros.couriertracking.entity.Store;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourierStoreVisitDto {
    private Long courierId;
    private Long storeId;
    private LocalDateTime visitTime;
}
