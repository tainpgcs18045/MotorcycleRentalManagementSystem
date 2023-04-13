package com.BikeHiringManagement.model.request;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class PaginationMaintainRequest extends PaginationRequest {
        private String username;
        private Date dateFrom;
        private Date dateTo;
}
