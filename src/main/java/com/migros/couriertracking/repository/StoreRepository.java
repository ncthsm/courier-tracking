package com.migros.couriertracking.repository;

import com.migros.couriertracking.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.locationtech.jts.geom.Point;
import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(value = "SELECT s.* FROM store s WHERE ST_DWithin(s.location, :point, :radius,false)", nativeQuery = true)
    List<Store> findStoresWithinRadius(@Param("point") Point point, @Param("radius") double radius);

}