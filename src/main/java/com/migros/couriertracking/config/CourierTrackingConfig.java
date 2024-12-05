package com.migros.couriertracking.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "tracking")
public class CourierTrackingConfig {

    private double entranceRadiusMeters;
    private int reentryThresholdMinutes;
    private String distanceCalculationType;
}
