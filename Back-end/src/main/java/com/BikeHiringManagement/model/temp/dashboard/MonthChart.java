package com.BikeHiringManagement.model.temp.dashboard;

import com.BikeHiringManagement.model.temp.MonthData;
import lombok.Data;

import java.util.List;

@Data
public class MonthChart {
    private List<MonthData> listMonthData;
}
