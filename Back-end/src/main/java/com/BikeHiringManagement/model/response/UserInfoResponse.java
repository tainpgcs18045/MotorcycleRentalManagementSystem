package com.BikeHiringManagement.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private String userId;
    private String username;
    private String name;
    private String email;
    private String role;
}
