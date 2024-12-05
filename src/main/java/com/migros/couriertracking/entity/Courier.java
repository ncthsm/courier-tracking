package com.migros.couriertracking.entity;

import com.migros.couriertracking.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table
@Entity
@EqualsAndHashCode(callSuper = true)
public class Courier extends BaseEntity {

    private String name;

    @Column(unique = true)
    private String identityNumber;

    private Double totalTravelDistance = 0.0;
}
