package com.BikeHiringManagement.model.response;

import lombok.Data;

@Data
public class BikeColorResponse {
    private Long id;
    private String name;

    public BikeColorResponse() {
    }

    public BikeColorResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}


