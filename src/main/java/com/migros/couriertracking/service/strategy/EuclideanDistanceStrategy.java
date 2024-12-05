package com.migros.couriertracking.service.strategy;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component("couriertracking.service.EuclideanDistanceStrategy")
public class EuclideanDistanceStrategy implements DistanceCalculationStrategy {

    @Override
    public double calculateDistance(Point point1, Point point2) {
        double lat1 = point1.getY();
        double lon1 = point1.getX();
        double lat2 = point2.getY();
        double lon2 = point2.getX();

        double dx = lat2 - lat1;
        double dy = lon2 - lon1;

        return Math.sqrt(dx * dx + dy * dy) * 111000;
    }
} 