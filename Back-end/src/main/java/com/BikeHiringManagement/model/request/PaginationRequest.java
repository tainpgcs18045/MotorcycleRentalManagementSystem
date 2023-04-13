package com.BikeHiringManagement.model.request;

import lombok.Data;

@Data
public class PaginationRequest {
    private String searchKey;
    private Integer page;
    private Integer limit;
    private String sortBy;
    private String sortType;
}
