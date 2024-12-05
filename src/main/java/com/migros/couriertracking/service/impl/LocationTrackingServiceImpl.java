package com.migros.couriertracking.service.impl;

import com.migros.couriertracking.config.CourierTrackingConfig;
import com.migros.couriertracking.entity.Courier;
import com.migros.couriertracking.entity.CourierLocation;
import com.migros.couriertracking.entity.CourierStoreVisit;
import com.migros.couriertracking.entity.Store;
import com.migros.couriertracking.event.LocationUpdateEvent;
import com.migros.couriertracking.model.request.CourierLocationRequest;
import com.migros.couriertracking.repository.CourierLocationRepository;
import com.migros.couriertracking.repository.CourierStoreVisitRepository;
import com.migros.couriertracking.service.CourierService;
import com.migros.couriertracking.service.LocationTrackingService;
import com.migros.couriertracking.service.StoreService;

import com.migros.couriertracking.exception.DuplicateVisitException;
import com.migros.couriertracking.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationTrackingServiceImpl implements LocationTrackingService {
    private final CourierLocationRepository locationRepository;
    private final CourierStoreVisitRepository visitRepository;
    private final StoreService storeService;
    private final ApplicationEventPublisher eventPublisher;
    private final CourierTrackingConfig courierTrackingConfig;
    private final CourierService courierService;

    @Override
    @Transactional
    public void trackLocation(Long courierId, CourierLocationRequest request) {
        CourierLocation location = createLocationFromRequest(courierId, request);
        CourierLocation lastLocation = locationRepository.findLastLocationByCourierId(courierId);

        locationRepository.save(location);
        eventPublisher.publishEvent(new LocationUpdateEvent(this, location, lastLocation));
    }

    @Override
    @Transactional
    public void checkStoreEntries(LocationUpdateEvent event) {
        CourierLocation courierLocation = event.getCourierLocation();
        List<Store> nearbyStores = storeService.findStoresNearLocation(courierLocation.getLocation());

        for (Store store : nearbyStores) {
            LocalDateTime threshold = courierLocation.getTimeStamp().minusMinutes(courierTrackingConfig.getReentryThresholdMinutes());
            boolean recentVisit = visitRepository.findLastVisit(courierLocation.getCourier().getId(), store.getId(), threshold).isPresent();

            if (recentVisit) {
                log.info("Courier {} has already visited store ", courierLocation.getCourier().getId());
                throw new DuplicateVisitException(
                        String.format("Courier %d has already visited store %s ", courierLocation.getCourier().getId(), store.getName()));
            }

            log.info("Courier {} visit {} store ,",courierLocation.getCourier().getId(),store.getName());

            CourierStoreVisit visit = new CourierStoreVisit();
            visit.setCourier(courierLocation.getCourier());
            visit.setStore(store);
            visit.setVisitTime(courierLocation.getTimeStamp());
            visitRepository.save(visit);
        }

    }

    private CourierLocation createLocationFromRequest(Long courierId, CourierLocationRequest request) {
        Courier courier = courierService.getCourierEntity(courierId);

        CourierLocation location = new CourierLocation();
        location.setCourier(courier);
        location.setLocation(GeometryUtil.createPoint(request.getLng(), request.getLat()));
        location.setTimeStamp(request.getTime());
        return location;
    }


}