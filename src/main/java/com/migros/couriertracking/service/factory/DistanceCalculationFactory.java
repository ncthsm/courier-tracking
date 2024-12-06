package com.migros.couriertracking.service.factory;

import com.migros.couriertracking.config.CourierTrackingConfig;
import com.migros.couriertracking.model.enums.DistanceCalculationType;
import com.migros.couriertracking.service.strategy.DistanceCalculationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DistanceCalculationFactory {

    private final ApplicationContext applicationContext;
    private final CourierTrackingConfig courierTrackingConfig;

    private final Map<String , String> DistanceCalculationServiceMap = Map.of(
            DistanceCalculationType.HAVERSINE.name(),"couriertracking.service.HaversineDistanceStrategy",
            DistanceCalculationType.EUCLIDEAN.name(),"couriertracking.service.EuclideanDistanceStrategy");

    public DistanceCalculationStrategy getDistanceCalculationStrategy() {
        String calculationServiceName = DistanceCalculationServiceMap.get(courierTrackingConfig.getDistanceCalculationType());
        if(calculationServiceName == null) {
            throw new IllegalStateException("CourierTracking service is not configured correctly");
        }
        return applicationContext.getBean(calculationServiceName, DistanceCalculationStrategy.class);
    }

}
