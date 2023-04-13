package com.BikeHiringManagement.controller.Authenticate;

import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.dto.PageDto;
import com.BikeHiringManagement.model.request.OrderRequest;
import com.BikeHiringManagement.model.request.PaginationOrderRequest;
import com.BikeHiringManagement.model.request.PaginationRequest;
import com.BikeHiringManagement.model.response.CartResponse;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.service.entity.OrderService;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    OrderService orderService;

    @PostMapping("/cart/add-bike")
    public ResponseEntity<?> cartAddBike(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = orderService.cartAddBike(username, orderRequest.getBikeId());
            if(result.getCode() == Constant.SUCCESS_CODE){
                return  responseUtils.getResponseEntity( result.getObject(), result.getCode(), result.getMessage(), HttpStatus.OK);
            }
            else{
                return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cart/get")
    public ResponseEntity<?> cartGetByUsername(HttpServletRequest request){
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = orderService.cartGetByUsername(username);
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

    @GetMapping("/cart/get/bike-number")
    public ResponseEntity<?> cartGetBikeNumber(HttpServletRequest request){
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = orderService.cartGetBikeNumber(username);
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

    @PostMapping("/cart/delete-bike/orderId={orderId}&bikeId={bikeId}")
    public ResponseEntity<?> cartDeleteBike(@PathVariable Long orderId,@PathVariable Long bikeId, HttpServletRequest request){
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = orderService.cartDeleteBike(orderId, bikeId, username);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cart/calculate-hiring-cost")
    public ResponseEntity<?> cartCalculateHiringCost(@RequestBody OrderRequest orderRequest) {
        try {
            Result result = orderService.cartCalculateHiringCost(orderRequest);
            if(result.getCode() == Constant.SUCCESS_CODE){
                return  responseUtils.getResponseEntity( result.getObject(), result.getCode(), result.getMessage(), HttpStatus.OK);
            }
            else{
                return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cart/save")
    public ResponseEntity<?> cartSave (@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = orderService.cartSave(orderRequest, username);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/get")
    public ResponseEntity<?> getOrderPagination(@RequestBody PaginationOrderRequest reqBody, HttpServletRequest request){
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            PageDto result = orderService.getOrderPagination(reqBody, username);
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
    public ResponseEntity<?> getOrderById(@RequestParam Long id){
        try{
            Result result = orderService.getOrderById(id);
            if(result.getCode() == Constant.SUCCESS_CODE){
                return  responseUtils.getResponseEntity( result.getObject(), result.getCode(), result.getMessage(), HttpStatus.OK);
            }
            else{
                return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
            }
        }catch(Exception e){
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/save")
    public ResponseEntity<?> saveOrder (@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = orderService.saveOrder(orderRequest, username);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder (@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Result result = orderService.cancelOrder(orderRequest, username);
            return responseUtils.getResponseEntity(null, result.getCode(), result.getMessage(), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
