package com.BikeHiringManagement.controller.Authenticate;

import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.dto.PageDto;
import com.BikeHiringManagement.model.request.BikeCategoryCreateRequest;
import com.BikeHiringManagement.model.request.DashboardRequest;
import com.BikeHiringManagement.model.request.PaginationRequest;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.service.entity.BikeCategoryService;
import com.BikeHiringManagement.service.entity.DashboardService;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/getByDateFromTo")
    public ResponseEntity<?> getDataByFromTo(@RequestBody DashboardRequest reqBody){
        try{
            Result result = dashboardService.getDataByFromTo(reqBody);
            if(result.getCode() == Constant.SUCCESS_CODE){
                return  responseUtils.getResponseEntity(result.getObject(), result.getCode(), result.getMessage(), HttpStatus.OK);
            }
            else{
                return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getByYear")
    public ResponseEntity<?> getDataByYear(@RequestBody DashboardRequest reqBody){
        try{
            Result result = dashboardService.getDataByYear(reqBody);
            if(result.getCode() == Constant.SUCCESS_CODE){
                return  responseUtils.getResponseEntity(result.getObject(), result.getCode(), result.getMessage(), HttpStatus.OK);
            }
            else{
                return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
