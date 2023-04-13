package com.BikeHiringManagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseDto {
    private Integer page;
    private Integer size;
    private String sort;
    private String sortBy;
}

