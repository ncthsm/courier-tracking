package com.migros.couriertracking.controller;

import com.migros.couriertracking.config.CourierApiV1;
import com.migros.couriertracking.model.dto.StoreDto;
import com.migros.couriertracking.model.request.CreateStoreRequest;
import com.migros.couriertracking.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(CourierApiV1.BASE_PATH_STORE)
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreDto> createStore(@Valid @RequestBody CreateStoreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.createStore(request));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable("storeId") Long storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDto> getStore(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStore(storeId));
    }

    @GetMapping
    public ResponseEntity<Page<StoreDto>> getAllStores(
            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getAllStores(PageRequest.of(page, pageSize)));
    }

}
