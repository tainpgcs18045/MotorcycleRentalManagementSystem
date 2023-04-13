package com.BikeHiringManagement.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "bike_color")
public class BikeColor extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
}
