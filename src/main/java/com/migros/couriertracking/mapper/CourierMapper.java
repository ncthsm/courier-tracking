package com.migros.couriertracking.mapper;

import com.migros.couriertracking.entity.Courier;
import com.migros.couriertracking.model.dto.CourierDto;
import com.migros.couriertracking.model.request.CreateCourierRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper
public interface CourierMapper {

    @Named("from")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalTravelDistance", constant = "0.0")
    Courier from(CreateCourierRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identityNumber", ignore = true)
    @Mapping(target = "totalTravelDistance", ignore = true)
    void updateEntityFromRequest(CreateCourierRequest request, @MappingTarget Courier courier);

    @Named("to")
    CourierDto to(Courier courier);
}
