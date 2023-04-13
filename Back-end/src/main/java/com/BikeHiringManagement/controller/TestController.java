package com.BikeHiringManagement.controller;

import com.BikeHiringManagement.dto.PageDto;
import com.BikeHiringManagement.model.request.PaginationRequest;
import com.BikeHiringManagement.service.TestService;
import com.BikeHiringManagement.service.entity.BikeManufacturerService;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/public/test")
public class TestController {
    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    TestService testService;

    @PostMapping("/run")
    public ResponseEntity<?> test(){
        try{
            testService.test();
            return responseUtils.getResponseEntity(null, 1, "OKE", HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

