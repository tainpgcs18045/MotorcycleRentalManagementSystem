package com.BikeHiringManagement.controller.Authenticate;


import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.dto.PageDto;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.model.request.BikeRequest;
import com.BikeHiringManagement.model.request.PaginationBikeRequest;
import com.BikeHiringManagement.model.response.BikeResponse;
import com.BikeHiringManagement.service.entity.BikeService;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/admin/bike")
public class BikeController {

    @Autowired
    BikeService bikeService;

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/get")
    public ResponseEntity<?> getBikePagination(@RequestBody PaginationBikeRequest reqBody, HttpServletRequest request){
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            reqBody.setUsername(username);
            PageDto result = bikeService.getBikePagination(reqBody);
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
    public ResponseEntity<?> getBikeById(@RequestParam Long bikeId){
        try{
            Result result = bikeService.getBikeById(bikeId);
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
    public ResponseEntity<?> createBike (@RequestBody BikeRequest bikeRequest,
                                         HttpServletRequest request) {

        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = bikeService.createBike(bikeRequest, username);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteBike(@PathVariable Long id, HttpServletRequest request){
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = bikeService.deleteBike(id, username);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateBike(@RequestBody BikeRequest reqBody,
                                                @PathVariable Long id,
                                                HttpServletRequest request){
        try{
            reqBody.setId(id);
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            reqBody.setUsername(username);
            Result result = bikeService.updateBike(reqBody);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/image/delete/{id}")
    public ResponseEntity<?> deleteBikeImage (@PathVariable Long id, HttpServletRequest request){
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = bikeService.deleteBikeImageById(id, username);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
