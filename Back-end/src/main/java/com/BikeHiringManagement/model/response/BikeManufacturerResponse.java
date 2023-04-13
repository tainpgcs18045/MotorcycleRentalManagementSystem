package com.BikeHiringManagement.model.response;

import lombok.Data;

@Data
public class BikeManufacturerResponse {
    private Long id;
    private String name;

    public BikeManufacturerResponse() {
    }

    public BikeManufacturerResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}


