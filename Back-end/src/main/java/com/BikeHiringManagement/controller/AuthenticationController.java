package com.BikeHiringManagement.controller;

import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.entity.Role;
import com.BikeHiringManagement.entity.User;
import com.BikeHiringManagement.model.temp.HistoryObject;
import com.BikeHiringManagement.model.request.SignupRequest;
import com.BikeHiringManagement.service.entity.HistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.BikeHiringManagement.model.request.LoginRequest;
import com.BikeHiringManagement.model.response.JwtResponse;
import com.BikeHiringManagement.model.response.UserInfoResponse;
import com.BikeHiringManagement.repository.RoleRepository;
import com.BikeHiringManagement.repository.UserRepository;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.service.system.SystemManager;
import com.BikeHiringManagement.service.system.UserDetailObject;
import com.BikeHiringManagement.utils.JwtUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.BikeHiringManagement.constant.Constant.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/authen")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    SystemManager systemManager;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    HistoryService historyService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            UserDetailObject userDetails = systemManager.login(loginRequest);
            if (userDetails != null) {
                if (userDetails.getResponseMessage().equals("user_not_exist")) {
                    return responseUtils.getResponseEntity(null, 0, "USER IS NOT EXIST", HttpStatus.OK);
                }
                if (userDetails.getResponseMessage().equals("wrong_password")) {
                    return responseUtils.getResponseEntity(null, 0, "WRONG PASSWORD", HttpStatus.OK);
                }
                List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                        .collect(Collectors.toList());
                UserInfoResponse userInfo = modelMapper.map(userDetails, UserInfoResponse.class);
                userInfo.setUserId(userDetails.getId());
                if (!roles.isEmpty()) {
                    userInfo.setRole(roles.get(0));
                }
                //
                HistoryObject historyObject = new HistoryObject();
                historyObject.setUsername(userDetails.getUsername());
                historyService.saveHistory(Constant.HISTORY_LOGIN, null, historyObject);

                return responseUtils.getResponseEntity(
                        new JwtResponse(userDetails.getJwt(), userInfo, roles),1,"Login success!", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "SERVER ERROR", HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return responseUtils.getResponseEntity(null, 0, "Error: Username is already taken!", HttpStatus.BAD_REQUEST);
            }
            User user = modelMapper.map(signUpRequest, User.class);
            user.setCreatedDate(new Date());
            user.setPassword(encoder.encode(user.getPassword()));

            Long roleId = signUpRequest.getRoleId();
            Set<Role> roles = systemManager.getSignUpRole(roleId);
            if(roles.isEmpty()){
                return responseUtils.getResponseEntity(null, 0, "Not found the Role ID", HttpStatus.BAD_REQUEST);
            }
            user.setRoles(roles);
            userRepository.save(user);

            return responseUtils.getResponseEntity(null, 1, "Create account successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, Constant.SYSTEM_ERROR_CODE, Constant.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
