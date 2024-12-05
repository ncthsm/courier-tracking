package com.migros.couriertracking.service.impl;

import com.migros.couriertracking.config.CourierTrackingConfig;
import com.migros.couriertracking.entity.Store;
import com.migros.couriertracking.exception.ResourceNotFoundException;
import com.migros.couriertracking.mapper.StoreMapper;
import com.migros.couriertracking.model.dto.StoreDto;
import com.migros.couriertracking.model.request.CreateStoreRequest;
import com.migros.couriertracking.repository.StoreRepository;
import com.migros.couriertracking.service.StoreService;
import com.migros.couriertracking.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final CourierTrackingConfig courierTrackingConfig;

    public StoreDto createStore(CreateStoreRequest createStoreRequest) {
        Store savedStore = storeRepository.save(storeMapper.from(createStoreRequest));
        return storeMapper.to(savedStore);
    }

    @Override
    @Transactional
    public void deleteStore(Long storeId) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + storeId));
        storeRepository.deleteById(storeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StoreDto> getAllStores(Pageable pageable) {
        return storeRepository.findAll(pageable).map(storeMapper::to);
    }

    @Override
    @Transactional(readOnly = true)
    public StoreDto getStore(Long storeId) {
        return storeRepository.findById(storeId)
                .map(storeMapper::to)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + storeId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Store> findStoresNearLocation(Point location) {
        return storeRepository.findStoresWithinRadius(location, courierTrackingConfig.getEntranceRadiusMeters());
    }

}
