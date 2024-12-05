package com.migros.couriertracking.event;

import com.migros.couriertracking.entity.CourierLocation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LocationUpdateEvent extends ApplicationEvent {
    private final CourierLocation courierLocation;
    private final CourierLocation previousLocation;

    public LocationUpdateEvent(Object source, CourierLocation courierLocation, CourierLocation previousLocation) {
        super(source);
        this.courierLocation = courierLocation;
        this.previousLocation = previousLocation;
    }
} 