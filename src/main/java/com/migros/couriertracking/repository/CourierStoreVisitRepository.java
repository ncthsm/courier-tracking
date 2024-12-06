package com.migros.couriertracking.repository;

import com.migros.couriertracking.entity.CourierStoreVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CourierStoreVisitRepository extends JpaRepository<CourierStoreVisit, Long> {
    
    @Query("SELECT csv FROM CourierStoreVisit csv WHERE csv.courier.id = :courierId AND csv.store.id = :storeId " +
           "AND csv.visitTime >= :fromTime ORDER BY csv.visitTime DESC LIMIT 1")
    Optional<CourierStoreVisit> findLastVisit(@Param("courierId") Long courierId,
                                             @Param("storeId") Long storeId,
                                             @Param("fromTime") LocalDateTime fromTime);

    Page<CourierStoreVisit> findAllByCourierId(Long storeId, Pageable pageable);
} 