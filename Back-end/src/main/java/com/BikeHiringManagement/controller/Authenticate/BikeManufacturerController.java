package com.BikeHiringManagement.controller.Authenticate;
import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.dto.PageDto;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.model.request.BikeManafacturerRequest;
import com.BikeHiringManagement.model.request.PaginationRequest;
import com.BikeHiringManagement.model.request.ObjectNameRequest;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.service.entity.BikeManufacturerService;
import com.BikeHiringManagement.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/bike-manufacturer")
public class BikeManufacturerController {
    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    BikeManufacturerService bikeManufacturerService;

    @PostMapping("/get")
    public ResponseEntity<?> getBikeManufacturerPagination(@RequestBody PaginationRequest reqBody){
        try{
            PageDto result = bikeManufacturerService.getBikeManufacturer(reqBody);
            if (result != null) {
                return responseUtils.getResponseEntity(result, Constant.SUCCESS_CODE, "Get Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, "Failed", HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getBikeManufacturerById(@RequestParam Long id) {
        try {
            Result result = bikeManufacturerService.getBikeManufacturerById(id);
            if(result.getCode() == Constant.SUCCESS_CODE){
                return  responseUtils.getResponseEntity( result.getObject(), result.getCode(), result.getMessage(), HttpStatus.OK);
            }
            else{
                return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
            }
        }catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBikeManafacturer(@RequestBody ObjectNameRequest reqBody,
                                             HttpServletRequest request) {
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            reqBody.setUsername(username);
            Result result = bikeManufacturerService.createBikeManufacturer(reqBody);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateBikeManufacturerCategory(@RequestBody BikeManafacturerRequest reqBody,
                                                            @PathVariable Long id,
                                                            HttpServletRequest request){
        try{
            reqBody.setId(id);
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            reqBody.setUsername(username);
            Result result = bikeManufacturerService.updateBikeManufacturer(reqBody);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteBikeManufacturer(@PathVariable Long id, HttpServletRequest request){
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = bikeManufacturerService.deleteBikeManufacturer(id, username);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

