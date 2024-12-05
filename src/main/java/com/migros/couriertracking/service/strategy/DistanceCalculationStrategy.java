package com.migros.couriertracking.service.strategy;
import org.locationtech.jts.geom.Point;

public interface DistanceCalculationStrategy {
    double calculateDistance(Point point1, Point point2);
}
