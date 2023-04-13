package com.BikeHiringManagement.model.request;

import lombok.Data;

import java.util.List;

@Data
public class BikeRequest {
    private Long id;
    private String username;
    private String name;
    private String bikeNo;
    private String bikeManualId;
    private Long bikeCategoryId;
    private Long bikeColorId;
    private Long bikeManufacturerId;
    private String status;
    private Integer hiredNumber;
    private List<AttachmentRequest> files;
}
