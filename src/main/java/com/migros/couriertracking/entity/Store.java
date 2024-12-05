package com.migros.couriertracking.entity;

import com.migros.couriertracking.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
@Table
@Entity
@EqualsAndHashCode(callSuper = true)
public class Store extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    public Double getLat() {
        return location != null ? location.getY() : null;
    }

    public Double getLng() {
        return location != null ? location.getX() : null;
    }
}
