package com.BikeHiringManagement.model.request;

import lombok.Data;

@Data
public class FilterOptionRequest {
    private String type;
    private Long value;
}
