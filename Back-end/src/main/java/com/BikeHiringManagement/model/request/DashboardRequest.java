package com.BikeHiringManagement.model.request;
import lombok.Data;

import java.util.Date;

@Data
public class DashboardRequest {
    private Date dateFrom;
    private Date dateTo;
    private String year;
}
