package com.BikeHiringManagement.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "bike_manufacturer")
public class BikeManufacturer extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
}

