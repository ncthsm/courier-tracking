package com.migros.couriertracking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migros.couriertracking.entity.Store;
import com.migros.couriertracking.model.request.CreateStoreRequest;
import com.migros.couriertracking.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreDataLoader {

    private final StoreService storeService;

    @Bean
    public CommandLineRunner loadStoreData() {
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("static/stores.json").getInputStream();
            List<Map<String, Object>> stores = mapper.readValue(inputStream, new TypeReference<>() {});
            stores.forEach(store->saveStore(store));
        };
    }

    private void saveStore(Map<String, Object> store){
        CreateStoreRequest request = new CreateStoreRequest();
        request.setName((String) store.get("name"));
        request.setLatitude(((Number) store.get("lat")).doubleValue());
        request.setLongitude(((Number) store.get("lng")).doubleValue());
        try{
            storeService.createStore(request);
        }catch (Exception e){
            log.info("Store already exists");
        }
    }

}
