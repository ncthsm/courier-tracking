package com.migros.couriertracking.service;

import com.migros.couriertracking.config.CourierTrackingConfig;
import com.migros.couriertracking.entity.Store;
import com.migros.couriertracking.exception.ResourceNotFoundException;
import com.migros.couriertracking.mapper.StoreMapper;
import com.migros.couriertracking.model.dto.StoreDto;
import com.migros.couriertracking.model.request.CreateStoreRequest;
import com.migros.couriertracking.repository.StoreRepository;
import com.migros.couriertracking.service.impl.StoreServiceImpl;
import com.migros.couriertracking.util.GeometryUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private StoreMapper storeMapper;
    @Spy
    private CourierTrackingConfig config;

    private StoreService storeService;

    @BeforeEach
    void setUp() {
        config.setEntranceRadiusMeters(100.0);
        storeService = new StoreServiceImpl(storeRepository, storeMapper, config);
    }

    @Test
    void createStore_WhenValidRequest_ShouldReturnStoreDto() {
        CreateStoreRequest request = getCreateStoreRequest();
        Store store = getStore();
        StoreDto storeDto = getStoreDto();

        when(storeMapper.from(any(CreateStoreRequest.class))).thenReturn(store);
        when(storeRepository.save(any(Store.class))).thenReturn(store);
        when(storeMapper.to(any(Store.class))).thenReturn(storeDto);

        StoreDto result = storeService.createStore(request);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(request.getName());
        verify(storeRepository).save(any(Store.class));
    }

    @Test
    void getStore_WhenStoreExists_ShouldReturnStoreDto() {
        Store store = getStore();
        StoreDto storeDto = getStoreDto();

        when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
        when(storeMapper.to(any(Store.class))).thenReturn(storeDto);

        StoreDto result = storeService.getStore(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(store.getId());
    }

    @Test
    void getStore_WhenStoreNotFound_ShouldThrowException() {
        when(storeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.getStore(1L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Store not found");
    }

    @Test
    void getAllStores_ShouldReturnPagedResult() {
        Store store = getStore();
        StoreDto storeDto = getStoreDto();
        Page<Store> storePage = new PageImpl<>(List.of(store));

        when(storeRepository.findAll(any(Pageable.class))).thenReturn(storePage);
        when(storeMapper.to(any(Store.class))).thenReturn(storeDto);

        Page<StoreDto> result = storeService.getAllStores(Pageable.unpaged());

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(store.getId());
    }

    @Test
    void findStoresNearLocation_ShouldReturnNearbyStores() {
        Store store = getStore();
        StoreDto storeDto = getStoreDto();
        Point searchLocation = GeometryUtil.createPoint(28.9784, 41.0082);

        when(storeRepository.findStoresWithinRadius(any(Point.class), eq(100.0)))
            .thenReturn(List.of(store));

        List<Store> result = storeService.findStoresNearLocation(searchLocation);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo(store.getName());
        verify(config).getEntranceRadiusMeters();
    }


    @Test
    void deleteStore_WhenStoreExists_ShouldDelete() {
        Store store = getStore();
        when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
        storeService.deleteStore(1L);

        verify(storeRepository).deleteById(1L);
    }

    @Test
    void deleteStore_WhenStoreNotFound_ShouldThrowException() {
        when(storeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.deleteStore(1L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Store not found");
    }

    private Store getStore() {
        Store store = new Store();
        store.setId(1L);
        store.setName("Test Store");
        store.setLocation(GeometryUtil.createPoint(28.9784, 41.0082));
        return store;
    }

    private StoreDto getStoreDto() {
        StoreDto dto = new StoreDto();
        dto.setId(1L);
        dto.setName("Test Store");
        dto.setLatitude(41.0082);
        dto.setLongitude(28.9784);
        return dto;
    }

    private CreateStoreRequest getCreateStoreRequest() {
        CreateStoreRequest request = new CreateStoreRequest();
        request.setName("Test Store");
        request.setLatitude(41.0082);
        request.setLongitude(28.9784);
        return request;
    }
} 