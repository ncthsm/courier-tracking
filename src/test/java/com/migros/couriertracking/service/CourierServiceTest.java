package com.migros.couriertracking.service;


import com.migros.couriertracking.entity.Courier;
import com.migros.couriertracking.exception.DuplicateIdentityNumberException;
import com.migros.couriertracking.exception.ResourceNotFoundException;
import com.migros.couriertracking.mapper.CourierMapper;
import com.migros.couriertracking.model.dto.CourierDto;
import com.migros.couriertracking.model.request.CreateCourierRequest;
import com.migros.couriertracking.repository.CourierRepository;
import com.migros.couriertracking.service.impl.CourierServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    private CourierRepository courierRepository;
    private CourierMapper courierMapper;
    private CourierService courierService;

    @BeforeEach
    void setUp() {
        courierRepository = mock(CourierRepository.class);
        courierMapper = mock(CourierMapper.class);
        courierService = new CourierServiceImpl(courierRepository, courierMapper);
    }

    @Test
    void createCourier_WhenValidRequest_ShouldReturnCourierDto() {
        CreateCourierRequest createRequest = getCourierRequest();
        Courier courier = getCourier();
        CourierDto courierDto = getCourierDto();

        when(courierRepository.findByIdentityNumber(anyString())).thenReturn(Optional.empty());
        when(courierMapper.from(any(CreateCourierRequest.class))).thenReturn(courier);
        when(courierRepository.save(any(Courier.class))).thenReturn(courier);
        when(courierMapper.to(any(Courier.class))).thenReturn(courierDto);

        CourierDto result = courierService.createCourier(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(createRequest.getName());
        assertThat(result.getIdentityNumber()).isEqualTo(createRequest.getIdentityNumber());
        verify(courierRepository).save(any(Courier.class));
    }

    @Test
    void createCourier_WhenDuplicateIdentityNumber_ShouldThrowException() {
        CreateCourierRequest createRequest = getCourierRequest();
        Courier courier = getCourier();

        when(courierRepository.findByIdentityNumber(anyString())).thenReturn(Optional.of(courier));

        assertThatThrownBy(() -> courierService.createCourier(createRequest))
            .isInstanceOf(DuplicateIdentityNumberException.class)
            .hasMessageContaining("Courier already exists with identity number");
    }

    @Test
    void getCourier_WhenCourierExists_ShouldReturnCourierDto() {
        Courier courier = getCourier();
        CourierDto courierDto = getCourierDto();
        when(courierRepository.findById(anyLong())).thenReturn(Optional.of(courier));
        when(courierMapper.to(any(Courier.class))).thenReturn(courierDto);

        CourierDto result = courierService.getCourier(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(courier.getId());
        assertThat(result.getName()).isEqualTo(courier.getName());
    }

    @Test
    void getCourier_WhenCourierNotFound_ShouldThrowException() {
        when(courierRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courierService.getCourier(1L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Courier not found");
    }

    @Test
    void getCourierByIdentityNumber_WhenExists_ShouldReturnCourierDto() {
        Courier courier = getCourier();
        CourierDto courierDto = getCourierDto();
        when(courierRepository.findByIdentityNumber(anyString())).thenReturn(Optional.of(courier));
        when(courierMapper.to(any(Courier.class))).thenReturn(courierDto);

        CourierDto result = courierService.getCourierByIdentityNumber("12345678901");

        assertThat(result).isNotNull();
        assertThat(result.getIdentityNumber()).isEqualTo(courier.getIdentityNumber());
    }

    @Test
    void getCourierByIdentityNumber_WhenNotFound_ShouldThrowException() {
        when(courierRepository.findByIdentityNumber(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courierService.getCourierByIdentityNumber("12345678901"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Courier not found with identity number");
    }

    @Test
    void getAllCouriers_ShouldReturnPagedResult() {
        Courier courier = getCourier();
        CourierDto courierDto = getCourierDto();
        Page<Courier> courierPage = new PageImpl<>(List.of(courier));

        when(courierRepository.findAll(any(Pageable.class))).thenReturn(courierPage);
        when(courierMapper.to(any(Courier.class))).thenReturn(courierDto);

        Page<CourierDto> result = courierService.getAllCouriers(Pageable.unpaged());

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(courier.getId());
    }

    @Test
    void updateCourierName_WhenCourierExists_ShouldUpdateAndReturnDto() {
        Courier courier = getCourier();
        CourierDto courierDto = getCourierDto();
        String newName = "Jane Doe";

        when(courierRepository.findById(anyLong())).thenReturn(Optional.of(courier));
        when(courierRepository.save(any(Courier.class))).thenReturn(courier);
        when(courierMapper.to(any(Courier.class))).thenReturn(courierDto);

        CourierDto result = courierService.updateCourierName(1L, newName);

        assertThat(result).isNotNull();
        verify(courierRepository).save(courier);
    }

    @Test
    void updateCourierName_WhenCourierNotFound_ShouldThrowException() {
        when(courierRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courierService.updateCourierName(1L, "New Name"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Courier not found");
    }

    @Test
    void deleteCourier_WhenCourierExists_ShouldDelete() {
        Courier courier = getCourier();
        when(courierRepository.findById(anyLong())).thenReturn(Optional.of(courier));

        courierService.deleteCourier(1L);

        verify(courierRepository).deleteById(1L);
    }

    @Test
    void deleteCourier_WhenCourierNotFound_ShouldThrowException() {
        when(courierRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courierService.deleteCourier(1L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Courier not found");
    }

    @Test
    void getTotalTravelDistance_WhenCourierExists_ShouldReturnDistance() {
        Courier courier = getCourier();
        courier.setTotalTravelDistance(100.0);

        when(courierRepository.findById(anyLong())).thenReturn(Optional.of(courier));

        Double distance = courierService.getTotalTravelDistance(1L);

        assertThat(distance).isEqualTo(100.0);
    }

    @Test
    void getTotalTravelDistance_WhenCourierNotFound_ShouldThrowException() {
        when(courierRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courierService.getTotalTravelDistance(1L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Courier not found");
    }

    @Test
    void updateTotalDistance_WhenCourierExists_ShouldUpdateDistance() {
        Courier courier = getCourier();
        courier.setTotalTravelDistance(100.0);

        when(courierRepository.findById(anyLong())).thenReturn(Optional.of(courier));
        when(courierRepository.save(any(Courier.class))).thenReturn(courier);

        courierService.updateTotalDistance(1L, 50.0);

        assertThat(courier.getTotalTravelDistance()).isEqualTo(150.0);
        verify(courierRepository).save(courier);
    }

    @Test
    void updateTotalDistance_WhenCourierNotFound_ShouldThrowException() {
        when(courierRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courierService.updateTotalDistance(1L, 50.0))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Courier not found");
    }

    @Test
    void getCourierEntity_WhenCourierExists_ShouldReturnCourier() {
        Courier courier = getCourier();
        when(courierRepository.findById(anyLong())).thenReturn(Optional.of(courier));

        Courier result = courierService.getCourierEntity(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(courier.getId());
        assertThat(result.getName()).isEqualTo(courier.getName());
    }

    @Test
    void getCourierEntity_WhenCourierNotFound_ShouldThrowException() {
        when(courierRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courierService.getCourierEntity(1L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Courier not found");
    }

    private Courier getCourier() {
        Courier courier = new Courier();
        courier.setId(1L);
        courier.setName("John Doe");
        courier.setIdentityNumber("12345678901");
        courier.setTotalTravelDistance(0.0);
        return courier;
    }

    private CourierDto getCourierDto() {
        CourierDto courierDto = new CourierDto();
        courierDto.setId(1L);
        courierDto.setName("John Doe");
        courierDto.setIdentityNumber("12345678901");
        courierDto.setTotalTravelDistance(0.0);
        return courierDto;
    }

    private CreateCourierRequest getCourierRequest() {
        CreateCourierRequest request = new CreateCourierRequest();
        request.setName("John Doe");
        request.setIdentityNumber("12345678901");
        return request;
    }
} 