package com.BikeHiringManagement.model.request;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MaintainRequest {
    private Long id;
    private Date date;
    private String title;
    private String description;
    private String type;
    private Double cost;
    private String stringListManualId;
    private List<BikeIDListRequest> bikeIDList;
}
