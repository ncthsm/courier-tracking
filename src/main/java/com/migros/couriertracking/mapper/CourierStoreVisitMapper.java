package com.migros.couriertracking.mapper;

import com.migros.couriertracking.entity.CourierStoreVisit;
import com.migros.couriertracking.model.dto.CourierStoreVisitDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface CourierStoreVisitMapper {

    @Named("to")
    @Mapping(target = "courierId", expression = "java(courierStoreVisit.getCourier().getId())")
    @Mapping(target = "storeId", expression = "java(courierStoreVisit.getStore().getId())")
    CourierStoreVisitDto to(CourierStoreVisit courierStoreVisit);

}
