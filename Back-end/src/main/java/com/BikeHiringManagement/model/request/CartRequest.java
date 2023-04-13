package com.BikeHiringManagement.model.request;

import com.BikeHiringManagement.model.response.BikeResponse;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CartRequest {
    public Long id;

    public Long customerId;
    public String customerName;
    public String phoneNumber;

    public List<BikeResponse> listBike;
    public Date expectedStartDate;
    public Date expectedEndDate;
    public Date actualStartDate;
    public Date actualEndDate;
    public Double calculatedCost;

    public Boolean isUsedService;
    public String serviceDescription;
    public Double serviceCost;

    public String depositType;
    public Double depositAmount;
    public String depositIdentifyCard;

    public String note;

    public Double totalAmount;
}
