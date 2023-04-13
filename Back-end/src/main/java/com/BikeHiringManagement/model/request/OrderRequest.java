package com.BikeHiringManagement.model.request;
import com.BikeHiringManagement.model.response.BikeResponse;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderRequest {
    private Long id;

    private String phoneNumber;
    private String tempCustomerPhone;
    private String tempCustomerName;

    private Date expectedStartDate;
    private Date expectedEndDate;
    private Date actualStartDate;
    private Date actualEndDate;
    private Double calculatedCost;

    private Boolean isUsedService;
    private String serviceDescription;
    private Double serviceCost;

    private String depositType;
    private Double depositAmount;
    private String depositIdentifyCard;
    private String depositHotel;

    private String note;
    private Double totalAmount;
    private String status;

    private Long bikeId;
    private Boolean isCreateOrder;
    private Boolean isCloseOrder;

}
