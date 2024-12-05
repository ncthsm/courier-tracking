package com.migros.couriertracking.service.impl;

import com.migros.couriertracking.entity.Courier;
import com.migros.couriertracking.exception.DuplicateIdentityNumberException;
import com.migros.couriertracking.exception.ResourceNotFoundException;
import com.migros.couriertracking.mapper.CourierMapper;
import com.migros.couriertracking.model.dto.CourierDto;
import com.migros.couriertracking.model.request.CreateCourierRequest;
import com.migros.couriertracking.repository.CourierRepository;
import com.migros.couriertracking.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;
    private final CourierMapper courierMapper;

    @Override
    @Transactional(readOnly = true)
    public Double getTotalTravelDistance(Long courierId) {
        return courierRepository.findById(courierId)
                .map(Courier::getTotalTravelDistance)
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found with id: " + courierId));
    }

    @Override
    public void updateTotalDistance(Long courierId, Double additionalDistance) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found with id: " + courierId));

        Double currentDistance = courier.getTotalTravelDistance();
        courier.setTotalTravelDistance(currentDistance + additionalDistance);
        courierRepository.save(courier);
    }

    @Override
    public CourierDto createCourier(CreateCourierRequest request) {
        courierRepository.findByIdentityNumber(request.getIdentityNumber())
                .ifPresent(c -> {
                    throw new DuplicateIdentityNumberException("Courier already exists with identity number: " + request.getIdentityNumber());
                });

        Courier courier = courierMapper.from(request);
        return courierMapper.to(courierRepository.save(courier));
    }

    @Override
    @Transactional
    public void deleteCourier(Long courierId) {
        courierRepository.findById(courierId)
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found with id: " + courierId));
        courierRepository.deleteById(courierId);
    }

    @Override
    @Transactional(readOnly = true)
    public CourierDto getCourier(Long courierId) {
        return courierRepository.findById(courierId)
                .map(courierMapper::to)
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found with id: " + courierId));
    }

    @Override
    @Transactional(readOnly = true)
    public CourierDto getCourierByIdentityNumber(String identityNumber) {
        return courierRepository.findByIdentityNumber(identityNumber)
                .map(courierMapper::to)
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found with identity number: " + identityNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourierDto> getAllCouriers(Pageable pageable) {
        return courierRepository.findAll(pageable).map(courierMapper::to);
    }

    @Override
    @Transactional
    public CourierDto updateCourierName(Long courierId, String newName) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found with id: " + courierId));

        CreateCourierRequest request = new CreateCourierRequest();
        request.setName(newName);

        courierMapper.updateEntityFromRequest(request, courier);
        return courierMapper.to(courierRepository.save(courier));
    }

    @Override
    @Transactional(readOnly = true)
    public Courier getCourierEntity(Long courierId) {
        return courierRepository.findById(courierId)
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found with id: " + courierId));
    }

}
