package com.BikeHiringManagement.model.temp;

import lombok.Data;

@Data
public class CustomerRank {
    private Long customerId;
    private String name;
    private String phoneNumber;
    private Integer hiredNumber;
    private Double hiredCost;
    private Integer rank;
}
