package com.BikeHiringManagement.model.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
//    private String id;
//    private String username;
//    private String firstname;
//    private String lastname;
//    private String email;
    private List<String> roles;
    private UserInfoResponse userInfo;

//    public JwtResponse(String accessToken, String id, String username, String firstname, String lastname, String email, List<String> roles) {
//        this.firstname = firstname;
//        this.lastname = lastname;
//        this.token = accessToken;
//        this.id = id;
//        this.username = username;
//        this.email = email;
//        this.roles = roles;
//    }

    public JwtResponse(String accessToken, UserInfoResponse userInfo, List<String> roles) {
        this.token = accessToken;
        this.userInfo = userInfo;
        this.roles = roles;
    }
}