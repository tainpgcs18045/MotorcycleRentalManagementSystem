package com.BikeHiringManagement.model.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PaginationOrderRequest extends PaginationRequest {
    private String status;
    private Date startDate;
    private Date endDate;
}
