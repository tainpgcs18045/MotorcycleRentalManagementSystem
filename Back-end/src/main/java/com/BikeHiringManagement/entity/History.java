package com.BikeHiringManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "history")
public class History implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "action_type", length = 50, nullable = false)
    private String actionType;

    @Column(name = "entity_name", length = 50)
    private String entityName;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "field_name", length = 100)
    private String fieldName;

    @Column(name = "previous_value", length = 100)
    private String previousValue;

    public History() {
    }

    public History(Long id, String userName, Date date, String actionType, String entityName, Long entityId, String fieldName, String previousValue, String updateValue) {
        this.id = id;
        this.userName = userName;
        this.date = date;
        this.actionType = actionType;
        this.entityName = entityName;
        this.entityId = entityId;
        this.fieldName = fieldName;
        this.previousValue = previousValue;
    }
}
