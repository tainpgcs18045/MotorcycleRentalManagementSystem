package com.BikeHiringManagement.model.response;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MaintainResponse {
    public Long id;
    public Date date;
    public String type;
    public String title;
    public String description;
    public Double cost;
    public String stringListManualId;
    public List<BikeResponse> listBike;

    public MaintainResponse() {
    }

    public MaintainResponse(Long id, Date date, String type, String title, Double cost) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.title = title;
        this.cost = cost;
    }
}
