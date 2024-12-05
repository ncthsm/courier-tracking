package com.migros.couriertracking.repository;

import com.migros.couriertracking.entity.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourierRepository extends JpaRepository<Courier, Long> {
    Optional<Courier> findByIdentityNumber(String identityNumber);
    Page<Courier> findAll(Pageable pageable);
}
