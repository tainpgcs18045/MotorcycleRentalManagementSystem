package com.BikeHiringManagement.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "customer")
public class Customer extends BaseEntity{

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "phone_number", length = 20, nullable = false)
    private String phoneNumber;

    @Column(name = "is_banned")
    private Boolean isBanned = Boolean.FALSE;
}
