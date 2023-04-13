package com.BikeHiringManagement.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "maintain")
public class Maintain extends BaseEntity{
    @Column(name = "date")
    private Date date;

    @Column(name = "type")
    private String type;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private Double cost;




}
