package com.migros.couriertracking.entity;

import com.migros.couriertracking.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Getter
@Setter
@Table
@Entity
@EqualsAndHashCode(callSuper = true)
public class CourierLocation extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    private LocalDateTime timeStamp;

    public Double getLat() {
        return location != null ? location.getY() : null;
    }

    public Double getLng() {
        return location != null ? location.getX() : null;
    }

}