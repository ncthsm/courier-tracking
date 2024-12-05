package com.migros.couriertracking.mapper;

import com.migros.couriertracking.entity.Store;
import com.migros.couriertracking.model.dto.StoreDto;
import com.migros.couriertracking.model.request.CreateStoreRequest;
import com.migros.couriertracking.util.GeometryUtil;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper( imports = {GeometryUtil.class})
public interface StoreMapper {

    @Named("from")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "location", expression = "java(GeometryUtil.createPoint(request.getLongitude(), request.getLatitude()))")
    Store from(CreateStoreRequest request);

    @Named("to")
    @Mapping(target = "latitude", expression = "java(store.getLocation().getY())")
    @Mapping(target = "longitude", expression = "java(store.getLocation().getX())")
    StoreDto to(Store store);
}
