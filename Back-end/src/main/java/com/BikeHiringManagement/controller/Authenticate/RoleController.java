package com.BikeHiringManagement.controller.Authenticate;


import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.service.entity.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/admin/role")
public class RoleController {

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<?> createRole(@RequestParam String name) {
        try{
            Result result = roleService.createRole(name);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
