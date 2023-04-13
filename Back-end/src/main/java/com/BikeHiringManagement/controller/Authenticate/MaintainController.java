package com.BikeHiringManagement.controller.Authenticate;
import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.dto.PageDto;
import com.BikeHiringManagement.model.request.*;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.service.entity.MaintainService;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/maintain")
public class MaintainController {
    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    MaintainService maintainService;

    @PostMapping("/get")
    public ResponseEntity<?> getMaintainPagination(@RequestBody PaginationMaintainRequest reqBody){
        try{
            PageDto result = maintainService.getMaintainPagination(reqBody);
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
    public ResponseEntity<?> getMaintainLogById(@RequestParam Long id) {
        try {
            Result result = maintainService.getMaintainById(id);
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
    public ResponseEntity<?> createMaintain(@RequestBody MaintainRequest maintainRequest,
                                             HttpServletRequest request) {
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = maintainService.createMaintain(maintainRequest, username);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateMaintain(@RequestBody MaintainRequest reqBody, HttpServletRequest request){
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = maintainService.updateMaintain(reqBody, username);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteMaintain(@PathVariable Long id, HttpServletRequest request){
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = maintainService.deleteMaintainById(id, username);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
