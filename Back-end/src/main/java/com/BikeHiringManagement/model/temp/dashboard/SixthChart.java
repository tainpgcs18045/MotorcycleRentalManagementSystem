package com.BikeHiringManagement.model.temp.dashboard;

import com.BikeHiringManagement.model.temp.CustomerRank;
import lombok.Data;

import java.util.List;

@Data
public class SixthChart {
    private List<CustomerRank> listTopCustomerHiringNumber;
    private List<CustomerRank> listTopCustomerHiringCost;
}
