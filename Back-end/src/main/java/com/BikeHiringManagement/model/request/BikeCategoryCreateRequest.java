package com.BikeHiringManagement.model.request;

import lombok.Data;

@Data
public class BikeCategoryCreateRequest {

    private Long id;
    private String name;
    private Double price;
    private String username;
}
