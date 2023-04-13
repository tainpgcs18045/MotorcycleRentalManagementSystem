package com.BikeHiringManagement.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "maintain_bike")
public class MaintainBike extends BaseEntity{
    @Column(name = "maintain_id", nullable = false)
    private Long maintainId;

    @Column(name = "bike_id", nullable = false)
    private Long bikeId;
}
