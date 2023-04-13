package com.BikeHiringManagement.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "orders")
public class Order extends BaseEntity{

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "temp_customer_name")
    private String tempCustomerName;

    @Column(name = "temp_customer_phone")
    private String tempCustomerPhone;

    @Column(name = "expected_start_date")
    private Date expectedStartDate;

    @Column(name = "expected_end_date")
    private Date expectedEndDate;

    @Column(name = "actual_start_date")
    private Date actualStartDate;

    @Column(name = "actual_end_date")
    private Date actualEndDate;

    @Column(name = "calculated_cost")
    private Double calculatedCost;

    @Column(name = "is_used_service")
    private Boolean isUsedService;

    @Column(name = "service_description")
    private String serviceDescription;

    @Column(name = "service_cost")
    private Double serviceCost;

    @Column(name = "deposit_type")
    private String depositType;

    @Column(name = "deposit_amount")
    private Double depositAmount;

    @Column(name = "deposit_identify_card")
    private String depositIdentifyCard;

    @Column(name = "deposit_hotel")
    private String depositHotel;

    @Column(name = "note")
    private String note;

    @Column(name = "status", length = 50, nullable = false)
    private String status = "IN CART";

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "is_used_month_hiring")
    private Boolean isUsedMonthHiring = Boolean.FALSE;

}
