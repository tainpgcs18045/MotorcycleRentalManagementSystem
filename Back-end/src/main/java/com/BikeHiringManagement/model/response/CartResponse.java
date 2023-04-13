package com.BikeHiringManagement.model.response;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;
@Data
public class CartResponse {
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

    private String depositType;
    private Double depositAmount;
    private String depositIdentifyCard;
    private String depositHotel;

    public String note;
    public Double totalAmount;

    public String status;
    private Boolean isUsedMonthHiring;
    private Integer bikeNumber;

    public CartResponse(){

    }

    public CartResponse(Long id, Long customerId, String customerName, String phoneNumber, Date expectedStartDate, Date expectedEndDate, Date actualStartDate, Date actualEndDate, String status, Double totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.expectedStartDate = expectedStartDate;
        this.expectedEndDate = expectedEndDate;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public CartResponse(Long id, Long customerId, String customerName, String phoneNumber, Date expectedStartDate, Date expectedEndDate, Date actualStartDate, Date actualEndDate, Double calculatedCost, Boolean isUsedService, String serviceDescription, Double serviceCost, String depositType, Double depositAmount, String depositIdentifyCard, String depositHotel, String note, Double totalAmount, String status) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.expectedStartDate = expectedStartDate;
        this.expectedEndDate = expectedEndDate;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
        this.calculatedCost = calculatedCost;
        this.isUsedService = isUsedService;
        this.serviceDescription = serviceDescription;
        this.serviceCost = serviceCost;
        this.depositType = depositType;
        this.depositAmount = depositAmount;
        this.depositIdentifyCard = depositIdentifyCard;
        this.depositHotel = depositHotel;
        this.note = note;
        this.totalAmount = totalAmount;
        this.status = status;
    }
}
