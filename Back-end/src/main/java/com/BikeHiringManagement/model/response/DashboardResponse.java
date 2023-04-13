package com.BikeHiringManagement.model.response;

import com.BikeHiringManagement.model.temp.dashboard.*;
import lombok.Data;

@Data
public class DashboardResponse {
    private FirstChart firstChart;
    private SecondChart secondChart;
    private ThirdChart thirdChart;
    private FourthChart fourthChart;
    private FifthChart fifthChart;
    private SixthChart sixthChart;
    private MonthChart monthChart;
}


