package com.migros.couriertracking.service;

import com.migros.couriertracking.config.CourierTrackingConfig;
import com.migros.couriertracking.entity.Courier;
import com.migros.couriertracking.entity.CourierLocation;
import com.migros.couriertracking.entity.CourierStoreVisit;
import com.migros.couriertracking.entity.Store;
import com.migros.couriertracking.event.LocationUpdateEvent;
import com.migros.couriertracking.exception.DuplicateVisitException;
import com.migros.couriertracking.model.request.CourierLocationRequest;
import com.migros.couriertracking.repository.CourierLocationRepository;
import com.migros.couriertracking.repository.CourierStoreVisitRepository;
import com.migros.couriertracking.service.impl.LocationTrackingServiceImpl;
import com.migros.couriertracking.util.GeometryUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationTrackingServiceTest {

    @Mock
    private CourierLocationRepository locationRepository;
    @Mock
    private CourierStoreVisitRepository visitRepository;
    @Mock
    private StoreService storeService;
    @Mock
    private CourierService courierService;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private CourierTrackingConfig config;
    private LocationTrackingService locationTrackingService;

    @BeforeEach
    void setUp() {

        locationTrackingService = new LocationTrackingServiceImpl(
            locationRepository,
            visitRepository,
            storeService,
            eventPublisher,
            config,
            courierService
        );
    }

    @Test
    void trackLocation_ShouldSaveLocationAndPublishEvent() {
        Long courierId = 1L;
        CourierLocationRequest request = createLocationRequest();
        Courier courier = createCourier();
        CourierLocation lastLocation = null;

        when(courierService.getCourierEntity(courierId)).thenReturn(courier);
        when(locationRepository.findLastLocationByCourierId(courierId)).thenReturn(lastLocation);

        locationTrackingService.trackLocation(courierId, request);

        verify(locationRepository).save(any(CourierLocation.class));
        verify(eventPublisher).publishEvent(any(LocationUpdateEvent.class));
    }

    @Test
    void checkStoreEntries_WhenNoRecentVisit_ShouldSaveVisit() {
        // given
        CourierLocation location = createCourierLocation();
        Store store = createStore();
        LocationUpdateEvent event = new LocationUpdateEvent(this, location, null);

        when(storeService.findStoresNearLocation(any(Point.class))).thenReturn(List.of(store));
        when(config.getReentryThresholdMinutes()).thenReturn(1);
        when(visitRepository.findLastVisit(anyLong(), anyLong(), any(LocalDateTime.class)))
            .thenReturn(Optional.empty());

        locationTrackingService.checkStoreEntries(event);

        verify(visitRepository).save(any(CourierStoreVisit.class));
    }

    @Test
    void checkStoreEntries_WhenRecentVisit_ShouldThrowException() {
        // given
        CourierLocation location = createCourierLocation();
        Store store = createStore();
        LocationUpdateEvent event = new LocationUpdateEvent(this, location, null);
        CourierStoreVisit recentVisit = new CourierStoreVisit();

        when(storeService.findStoresNearLocation(any(Point.class))).thenReturn(List.of(store));
        when(config.getReentryThresholdMinutes()).thenReturn(1);
        when(visitRepository.findLastVisit(anyLong(), anyLong(), any(LocalDateTime.class)))
            .thenReturn(Optional.of(recentVisit));

        // when & then
        assertThatThrownBy(() -> locationTrackingService.checkStoreEntries(event))
            .isInstanceOf(DuplicateVisitException.class)
            .hasMessageContaining("has already visited store");
    }

    private CourierLocationRequest createLocationRequest() {
        CourierLocationRequest request = new CourierLocationRequest();
        request.setLat(41.0082);
        request.setLng(28.9784);
        request.setTime(LocalDateTime.now());
        return request;
    }

    private Courier createCourier() {
        Courier courier = new Courier();
        courier.setId(1L);
        courier.setName("Test Courier");
        return courier;
    }

    private Store createStore() {
        Store store = new Store();
        store.setId(1L);
        store.setName("Test Store");
        store.setLocation(GeometryUtil.createPoint(28.9784, 41.0082));
        return store;
    }

    private CourierLocation createCourierLocation() {
        CourierLocation location = new CourierLocation();
        location.setCourier(createCourier());
        location.setLocation(GeometryUtil.createPoint(28.9784, 41.0082));
        location.setTimeStamp(LocalDateTime.now());
        return location;
    }
} 