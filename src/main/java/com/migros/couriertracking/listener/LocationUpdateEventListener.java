package com.migros.couriertracking.listener;

import com.migros.couriertracking.event.LocationUpdateEvent;
import com.migros.couriertracking.service.LocationTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationUpdateEventListener {
    
    private final LocationTrackingService locationTrackingService;

    @EventListener
    public void handleLocationUpdate(LocationUpdateEvent event) {
        locationTrackingService.checkStoreEntries(event);
    }
} 