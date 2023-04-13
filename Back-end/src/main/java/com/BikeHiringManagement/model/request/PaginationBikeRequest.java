package com.BikeHiringManagement.model.request;

import lombok.Data;

import java.util.List;

@Data
public class PaginationBikeRequest extends PaginationRequest {
    private String username;
    private Long categoryId;
    private Boolean isInCart;
    private List<FilterOptionRequest> filterList;
}
