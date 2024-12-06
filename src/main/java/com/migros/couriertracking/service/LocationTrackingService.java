package com.migros.couriertracking.service;

import com.migros.couriertracking.event.LocationUpdateEvent;
import com.migros.couriertracking.model.dto.CourierStoreVisitDto;
import com.migros.couriertracking.model.request.CourierLocationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface LocationTrackingService {
    void trackLocation(Long courierId,CourierLocationRequest request);
    void checkStoreEntries(LocationUpdateEvent event);
    Page<CourierStoreVisitDto> getStoreVisit(Long courierId, PageRequest pageRequest);
}