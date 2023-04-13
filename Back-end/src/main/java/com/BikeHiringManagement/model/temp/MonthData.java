package com.BikeHiringManagement.model.temp;

import lombok.Data;

@Data
public class MonthData {
    private Integer month;
    private Integer totalOrder;
    private Integer totalNewCustomer;
    private Double totalIncome;
    private Double totalRevenue;
    private Double totalExpense;

}
