package com.BikeHiringManagement.model.response;
import lombok.Data;

@Data
public class FormulaCoefficientResponse {
    public Double coefficient;
    public Double lowerLimit;
    public Double upperLimit;
}
