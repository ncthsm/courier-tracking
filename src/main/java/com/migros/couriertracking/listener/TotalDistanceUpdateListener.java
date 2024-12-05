package com.migros.couriertracking.listener;

import com.migros.couriertracking.event.LocationUpdateEvent;
import com.migros.couriertracking.service.CourierService;
import com.migros.couriertracking.service.factory.DistanceCalculationFactory;
import com.migros.couriertracking.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TotalDistanceUpdateListener {
    private final CourierService courierService;
    private final DistanceCalculationFactory distanceCalculationFactory;

    @EventListener
    public void handleLocationUpdate(LocationUpdateEvent event) {
        if (event.getPreviousLocation() != null) {
            double distance = distanceCalculationFactory.getDistanceCalculationStrategy().calculateDistance(event.getPreviousLocation().getLocation(), event.getCourierLocation().getLocation());
            courierService.updateTotalDistance(
                event.getCourierLocation().getCourier().getId(), 
                distance
            );
        }
    }
} 