package com.migros.couriertracking.controller;

import com.migros.couriertracking.controller.base.BaseIntegrationTest;
import com.migros.couriertracking.model.request.CreateStoreRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@Transactional
public class StoreControllerIntegrationTest extends BaseIntegrationTest {


    @Test
    void shouldCreateStore() throws Exception {
        CreateStoreRequest request = new CreateStoreRequest();
        request.setName("Test Store");
        request.setLatitude(41.0082);
        request.setLongitude(28.9784);

        mockMvc.perform(post("/api/v1/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test Store")))
                .andExpect(jsonPath("$.latitude", is(41.0082)))
                .andExpect(jsonPath("$.longitude", is(28.9784)))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void shouldGetStoreById() throws Exception {
        CreateStoreRequest createRequest = new CreateStoreRequest();
        createRequest.setName("Get Store Test");
        createRequest.setLatitude(41.0082);
        createRequest.setLongitude(28.9784);

        String createResponse = mockMvc.perform(post("/api/v1/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long storeId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/v1/store/{storeId}", storeId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Get Store Test")))
                .andExpect(jsonPath("$.latitude", is(41.0082)))
                .andExpect(jsonPath("$.longitude", is(28.9784)));
    }

    @Test
    void shouldReturnNotFoundForNonExistingStore() throws Exception {
        mockMvc.perform(get("/api/v1/store/{storeId}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldValidateCreateStoreRequest() throws Exception {
        CreateStoreRequest invalidRequest = new CreateStoreRequest();

        mockMvc.perform(post("/api/v1/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(greaterThan(0))));
    }

    @Test
    void shouldValidateLatitudeBounds() throws Exception {
        CreateStoreRequest invalidRequest = new CreateStoreRequest();
        invalidRequest.setName("Invalid Store");
        invalidRequest.setLatitude(91.0);
        invalidRequest.setLongitude(28.9784);

        mockMvc.perform(post("/api/v1/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(containsString("latitude"))));
    }

    @Test
    void shouldValidateLongitudeBounds() throws Exception {
        CreateStoreRequest invalidRequest = new CreateStoreRequest();
        invalidRequest.setName("Invalid Store");
        invalidRequest.setLatitude(41.0082);
        invalidRequest.setLongitude(181.0);

        mockMvc.perform(post("/api/v1/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(containsString("longitude"))));
    }

    @Test
    void shouldDeleteStore() throws Exception {
        CreateStoreRequest createRequest = new CreateStoreRequest();
        createRequest.setName("Store To Delete");
        createRequest.setLatitude(41.0082);
        createRequest.setLongitude(28.9784);

        String createResponse = mockMvc.perform(post("/api/v1/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long storeId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(delete("/api/v1/store/{storeId}", storeId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/store/{storeId}", storeId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingStore() throws Exception {
        mockMvc.perform(delete("/api/v1/store/{storeId}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
} 