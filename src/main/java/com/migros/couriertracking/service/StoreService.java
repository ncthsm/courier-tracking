package com.migros.couriertracking.service;

import com.migros.couriertracking.entity.Store;
import com.migros.couriertracking.model.dto.StoreDto;
import com.migros.couriertracking.model.request.CreateStoreRequest;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {
    StoreDto createStore(CreateStoreRequest createStoreRequest);
    void deleteStore(Long storeId);
    StoreDto getStore(Long storeId);
    Page<StoreDto> getAllStores(Pageable pageable);
    List<Store> findStoresNearLocation(Point location);
}
