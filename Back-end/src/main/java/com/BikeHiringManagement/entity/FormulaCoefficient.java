package com.BikeHiringManagement.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "formula_coefficient")
public class FormulaCoefficient extends BaseEntity{

    @Column(name = "formula_id")
    private Long formulaId;

    @Column(name = "lower_limit")
    private Double lowerLimit;

    @Column(name = "upper_limit")
    private Double upperLimit;

    @Column(name = "coefficient")
    private Double coefficient;

}
