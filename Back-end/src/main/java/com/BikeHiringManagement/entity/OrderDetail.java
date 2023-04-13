package com.BikeHiringManagement.entity;


import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "order_detail")
public class OrderDetail extends BaseEntity{

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "bike_id", nullable = false)
    private Long bikeId;
}
