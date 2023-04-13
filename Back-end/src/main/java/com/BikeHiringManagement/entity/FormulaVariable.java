package com.BikeHiringManagement.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "formula_variable")
public class FormulaVariable extends BaseEntity{

    @Column(name = "formula_id")
    private Long formulaId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

}
