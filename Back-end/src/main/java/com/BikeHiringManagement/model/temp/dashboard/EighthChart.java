package com.BikeHiringManagement.model.temp.dashboard;

import com.BikeHiringManagement.model.temp.MonthData;
import lombok.Data;

import java.util.List;

@Data
public class EighthChart {
    private List<MonthData> listTotalRevenueByMonth;
    private List<MonthData> listTotalExpenseByMonth;
    private List<MonthData> listTotalProfitByMonth;
}
