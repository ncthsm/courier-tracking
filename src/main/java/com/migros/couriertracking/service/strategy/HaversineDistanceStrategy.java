package com.migros.couriertracking.service.strategy;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component("couriertracking.service.HaversineDistanceStrategy")
public class HaversineDistanceStrategy implements DistanceCalculationStrategy {
    private static final double EARTH_RADIUS = 6371;

    @Override
    public double calculateDistance(Point point1, Point point2) {
        double lat1 = point1.getY();
        double lon1 = point1.getX();
        double lat2 = point2.getY();
        double lon2 = point2.getX();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.sin(dLon / 2) * Math.sin(dLon / 2) * 
                   Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c * 1000;
    }
} 