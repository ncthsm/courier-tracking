package com.migros.couriertracking.controller;

import com.migros.couriertracking.config.CourierApiV1;
import com.migros.couriertracking.model.dto.CourierDto;
import com.migros.couriertracking.model.request.CourierLocationRequest;
import com.migros.couriertracking.model.request.CreateCourierRequest;
import com.migros.couriertracking.model.response.CourierDistanceResponse;
import com.migros.couriertracking.service.CourierService;
import com.migros.couriertracking.service.LocationTrackingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(CourierApiV1.BASE_PATH_COURIER)
public class CourierController {

    private final CourierService courierService;
    private final LocationTrackingService locationTrackingService;

    @PostMapping
    public ResponseEntity<CourierDto> createCourier(@Valid @RequestBody CreateCourierRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courierService.createCourier(request));
    }

    @DeleteMapping("/{courierId}")
    public ResponseEntity<Void> deleteCourier(@PathVariable("courierId") Long courierId) {
        courierService.deleteCourier(courierId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{courierId}")
    public ResponseEntity<CourierDto> getCourier(@PathVariable("courierId") Long courierId) {
        return ResponseEntity.ok(courierService.getCourier(courierId));
    }

    @GetMapping
    public ResponseEntity<Page<CourierDto>> getAllCouriers(
            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        return ResponseEntity.ok(courierService.getAllCouriers(PageRequest.of(page, pageSize)));
    }

    @PutMapping("/{courierId}")
    public ResponseEntity<CourierDto> updateCourierName(
            @PathVariable("courierId") Long courierId,
            @Valid @RequestBody CreateCourierRequest request) {
        return ResponseEntity.ok(courierService.updateCourierName(courierId, request.getName()));
    }

    @GetMapping("/identity/{identityNumber}")
    public ResponseEntity<CourierDto> getCourierByIdentityNumber(@PathVariable("identityNumber") String identityNumber) {
        return ResponseEntity.ok(courierService.getCourierByIdentityNumber(identityNumber));
    }

    @GetMapping("/{courierId}/total-distance")
    public ResponseEntity<CourierDistanceResponse> getTotalDistance(@PathVariable("courierId") Long courierId) {
        Double totalDistance = courierService.getTotalTravelDistance(courierId);
        return ResponseEntity.ok(new CourierDistanceResponse(courierId, totalDistance));
    }


    @PostMapping( "/{courierId}/locations")
    public ResponseEntity<Void> trackLocation(
            @PathVariable(name = "courierId") Long courierId,
            @Valid @RequestBody CourierLocationRequest request) {
        locationTrackingService.trackLocation(courierId,request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
