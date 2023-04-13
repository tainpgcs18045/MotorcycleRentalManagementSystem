package com.BikeHiringManagement.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "bike_image")
public class BikeImage extends BaseEntity{

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "path", length = 250, nullable = false)
    private String path;

    @Column(name = "bike_id", nullable = false)
    private Long bikeId;

}
