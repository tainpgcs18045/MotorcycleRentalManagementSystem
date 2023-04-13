package com.BikeHiringManagement.controller.Authenticate;
import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.dto.PageDto;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.model.request.BikeColorRequest;
import com.BikeHiringManagement.model.request.PaginationRequest;
import com.BikeHiringManagement.model.request.ObjectNameRequest;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.service.entity.BikeColorService;
import com.BikeHiringManagement.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/bike-color")
public class BikeColorController {
    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    BikeColorService bikeColorService;

    @PostMapping("/get")
    public ResponseEntity<?> getBikeColorPagination(@RequestBody PaginationRequest reqBody){
        try{
            PageDto result = bikeColorService.getBikeColor(reqBody);
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
    public ResponseEntity<?> getBikeColorById(@RequestParam  Long id) {
        try {
            Result result = bikeColorService.getBikeColorById(id);
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
    public ResponseEntity<?> createBikeColor(@RequestBody ObjectNameRequest reqBody,
                                             HttpServletRequest request) {
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            reqBody.setUsername(username);
            Result result = bikeColorService.createBikeColor(reqBody);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateBikeColorCategory(@RequestBody BikeColorRequest reqBody,
                                                     @PathVariable Long id,
                                                     HttpServletRequest request){
        try{
            reqBody.setId(id);
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            reqBody.setUsername(username);
            Result result = bikeColorService.updateBikeColor(reqBody);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteBikeColor(@PathVariable Long id, HttpServletRequest request){
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = bikeColorService.deleteBikeColor(id, username);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
