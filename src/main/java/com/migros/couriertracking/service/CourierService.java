package com.migros.couriertracking.service;

import com.migros.couriertracking.entity.Courier;
import com.migros.couriertracking.model.dto.CourierDto;
import com.migros.couriertracking.model.request.CreateCourierRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourierService {
    Double getTotalTravelDistance(Long courierId);
    CourierDto createCourier(CreateCourierRequest request);
    void deleteCourier(Long courierId);
    CourierDto getCourier(Long courierId);
    CourierDto getCourierByIdentityNumber(String identityNumber);
    Page<CourierDto> getAllCouriers(Pageable pageable);
    CourierDto updateCourierName(Long courierId, String newName);
    void updateTotalDistance(Long courierId, Double additionalDistance);
    Courier getCourierEntity(Long courierId);
}
