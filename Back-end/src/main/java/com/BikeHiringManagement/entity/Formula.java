package com.BikeHiringManagement.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "formula")
public class Formula extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "formula")
    private String formula;

}
