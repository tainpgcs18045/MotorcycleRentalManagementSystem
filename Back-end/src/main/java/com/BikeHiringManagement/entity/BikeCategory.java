package com.BikeHiringManagement.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "bike_category")

public class BikeCategory extends BaseEntity{

    @Column(name = "name", length = 100, nullable = true)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

}
