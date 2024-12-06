package com.migros.couriertracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migros.couriertracking.controller.base.BaseIntegrationTest;
import com.migros.couriertracking.model.request.CreateCourierRequest;
import com.migros.couriertracking.model.request.CourierLocationRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@Transactional
public class CourierControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldCreateCourier() throws Exception {
        CreateCourierRequest request = new CreateCourierRequest();
        request.setName("Test Courier");
        request.setIdentityNumber("12345678901");

        mockMvc.perform(post("/api/v1/courier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test Courier")))
                .andExpect(jsonPath("$.identityNumber", is("12345678901")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void shouldTrackCourierLocation() throws Exception {
        CreateCourierRequest createRequest = new CreateCourierRequest();
        createRequest.setName("Location Test Courier");
        createRequest.setIdentityNumber("12345678902");

        String createResponse = mockMvc.perform(post("/api/v1/courier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long courierId = objectMapper.readTree(createResponse).get("id").asLong();

        CourierLocationRequest locationRequest = new CourierLocationRequest();
        locationRequest.setLat(41.0082);
        locationRequest.setLng(28.9784);
        locationRequest.setTime(LocalDateTime.now());

        mockMvc.perform(post("/api/v1/courier/{courierId}/locations", courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetTotalTravelDistance() throws Exception {
        CreateCourierRequest createRequest = new CreateCourierRequest();
        createRequest.setName("Distance Test Courier");
        createRequest.setIdentityNumber("12345678903");

        String createResponse = mockMvc.perform(post("/api/v1/courier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long courierId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/v1/courier/{courierId}/total-distance", courierId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDistance", notNullValue()));
    }

    @Test
    void shouldReturnNotFoundForNonExistingCourier() throws Exception {
        mockMvc.perform(get("/api/v1/courier/{courierId}/total-distance", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldValidateCreateCourierRequest() throws Exception {
        CreateCourierRequest invalidRequest = new CreateCourierRequest();

        mockMvc.perform(post("/api/v1/courier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(containsString("Identity number cannot be empty"))));
    }

    @Test
    void shouldValidateCourierLocationRequest() throws Exception {
        CourierLocationRequest invalidRequest = new CourierLocationRequest();

        mockMvc.perform(post("/api/v1/courier/{courierId}/locations", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotThrowDuplicateVisitWithinOneHour() throws Exception {
        CreateCourierRequest createRequest = new CreateCourierRequest();
        createRequest.setName("Duplicate Visit Test Courier");
        createRequest.setIdentityNumber("12345678904");

        String createResponse = mockMvc.perform(post("/api/v1/courier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long courierId = objectMapper.readTree(createResponse).get("id").asLong();

        CourierLocationRequest firstVisit = new CourierLocationRequest();
        firstVisit.setLat(40.9923307);
        firstVisit.setLng(29.1244229);
        firstVisit.setTime(LocalDateTime.now());

        mockMvc.perform(post("/api/v1/courier/{courierId}/locations", courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstVisit)))
                .andExpect(status().isOk());

        CourierLocationRequest secondVisit = new CourierLocationRequest();
        secondVisit.setLat(40.9923307);
        secondVisit.setLng(29.1244229);
        secondVisit.setTime(LocalDateTime.now().plusMinutes(30));

        mockMvc.perform(post("/api/v1/courier/{courierId}/locations", courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondVisit)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowDuplicateVisitWithinOneHour() throws Exception {
        CreateCourierRequest createRequest = new CreateCourierRequest();
        createRequest.setName("Duplicate Visit Test Courier");
        createRequest.setIdentityNumber("12345678904");

        String createResponse = mockMvc.perform(post("/api/v1/courier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long courierId = objectMapper.readTree(createResponse).get("id").asLong();

        CourierLocationRequest firstVisit = new CourierLocationRequest();
        firstVisit.setLat(40.9923307);
        firstVisit.setLng(29.1244229);
        firstVisit.setTime(LocalDateTime.now());

        mockMvc.perform(post("/api/v1/courier/{courierId}/locations", courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstVisit)))
                .andExpect(status().isOk());

        CourierLocationRequest secondVisit = new CourierLocationRequest();
        secondVisit.setLat(40.9923307);
        secondVisit.setLng(29.1244229);
        secondVisit.setTime(LocalDateTime.now());

        mockMvc.perform(post("/api/v1/courier/{courierId}/locations", courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondVisit)))
                .andExpect(status().isConflict());
    }
}