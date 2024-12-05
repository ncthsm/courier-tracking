package com.migros.couriertracking.entity;

import com.migros.couriertracking.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table
@Entity
@EqualsAndHashCode(callSuper = true)
public class CourierStoreVisit extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private LocalDateTime visitTime;
}
