package com.migros.couriertracking.repository;

import com.migros.couriertracking.entity.CourierLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourierLocationRepository extends JpaRepository<CourierLocation, Long> {
    @Query("SELECT cl FROM CourierLocation cl WHERE cl.courier.id = :courierId ORDER BY cl.createdAt")
    List<CourierLocation> findByCourierIdOrderByTimestamp(@Param("courierId") Long courierId);

    @Query("SELECT cl FROM CourierLocation cl WHERE cl.courier.id = :courierId ORDER BY cl.createdAt DESC LIMIT 1")
    CourierLocation findLastLocationByCourierId(@Param("courierId") Long courierId);
} 