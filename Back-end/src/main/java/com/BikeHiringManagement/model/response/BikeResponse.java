package com.BikeHiringManagement.model.response;

import com.BikeHiringManagement.dto.PageDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BikeResponse {

    private Long id;
    private String name;
    private String bikeManualId;
    private String bikeNo;
    private Integer hiredNumber;
    private Long bikeCategoryId;
    private String bikeCategoryName;
    private Double price;
    private Long bikeColorId;
    private String bikeColor;
    private Long bikeManufacturerId;
    private String bikeManufacturerName;
    private String status;
    private String createdUser;
    private Date createdDate;
    private String modifiedUser;
    private Date modifiedDate;
    private Long orderId;
    private List<AttachmentResponse> imageList;
    private List<BikeResponse> listBike;

    public BikeResponse() {
    }

    public BikeResponse(Long id, String name, String bikeManualId, String bikeNo, Integer hiredNumber, Long bikeCategoryId, String bikeCategoryName, Double price, Long bikeColorId, String bikeColor, Long bikeManufacturerId, String bikeManufacturerName, String status) {
        this.id = id;
        this.name = name;
        this.bikeManualId = bikeManualId;
        this.bikeNo = bikeNo;
        this.hiredNumber = hiredNumber;
        this.bikeCategoryId = bikeCategoryId;
        this.bikeCategoryName = bikeCategoryName;
        this.price = price;
        this.bikeColorId = bikeColorId;
        this.bikeColor = bikeColor;
        this.bikeManufacturerId = bikeManufacturerId;
        this.bikeManufacturerName = bikeManufacturerName;
        this.status = status;
    }

    public BikeResponse(Long id, String name, String bikeManualId, String bikeNo, Integer hiredNumber, Long bikeCategoryId, String bikeCategoryName, Double price, Long bikeColorId, String bikeColor, Long bikeManufacturerId, String bikeManufacturerName, String status, String createdUser, Date createdDate, String modifiedUser, Date modifiedDate) {
        this.id = id;
        this.name = name;
        this.bikeManualId = bikeManualId;
        this.bikeNo = bikeNo;
        this.hiredNumber = hiredNumber;
        this.bikeCategoryId = bikeCategoryId;
        this.bikeCategoryName = bikeCategoryName;
        this.price = price;
        this.bikeColorId = bikeColorId;
        this.bikeColor = bikeColor;
        this.bikeManufacturerId = bikeManufacturerId;
        this.bikeManufacturerName = bikeManufacturerName;
        this.status = status;
        this.createdUser = createdUser;
        this.createdDate = createdDate;
        this.modifiedUser = modifiedUser;
        this.modifiedDate = modifiedDate;
    }

}
