package com.migros.couriertracking.service;

import com.migros.couriertracking.event.LocationUpdateEvent;
import com.migros.couriertracking.model.request.CourierLocationRequest;

public interface LocationTrackingService {
    void trackLocation(Long courierId,CourierLocationRequest request);
    void checkStoreEntries(LocationUpdateEvent event);
} 