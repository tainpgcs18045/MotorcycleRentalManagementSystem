package com.BikeHiringManagement.model.request;

import com.BikeHiringManagement.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {

    private String userId;
    private String username;
    private String email;
    private String name;
    private String gender;
    private Long age;
    private String phone;
    private String address;
    private Set<Role> roles;
    private Long roleId;
    private String password;

}
