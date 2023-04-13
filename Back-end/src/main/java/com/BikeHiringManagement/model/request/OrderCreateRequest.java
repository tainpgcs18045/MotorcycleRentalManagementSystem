package com.BikeHiringManagement.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class OrderCreateRequest {
    private Date startDate;
    private Date endDate;
    private String customerName;
    private String phoneNumber;
    private String bikeCategoryId;

}
