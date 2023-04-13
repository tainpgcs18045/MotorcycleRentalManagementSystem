package com.BikeHiringManagement.service.system;

import com.BikeHiringManagement.entity.Role;
import com.BikeHiringManagement.entity.User;
import com.BikeHiringManagement.model.request.LoginRequest;
import com.BikeHiringManagement.repository.RoleRepository;
import com.BikeHiringManagement.repository.UserRepository;
import com.BikeHiringManagement.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class SystemManager {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    public UserDetailObject login(LoginRequest request) {
        try {
//            String username = request.getEmail().substring(0, request.getEmail().indexOf("@"));
            UserDetailObject userDetails = new UserDetailObject();
            Optional<User> existUser = userRepository.findByUsername(request.getUsername());
            if (existUser == null || existUser.isEmpty()) {
                userDetails.setResponseMessage("user_not_exist");
                return userDetails;
            } else {
                try {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String jwt = jwtUtils.generateJwtToken(authentication);
                    userDetails = (UserDetailObject) authentication.getPrincipal();
                    userDetails.setJwt(jwt);
                    userDetails.setResponseMessage("success");
                    userDetails.setName(existUser.get().getName());
                    return userDetails;
                } catch (Exception e) {
                    userDetails.setResponseMessage("wrong_password");
                    return userDetails;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Map<String, Object>> getRole() {
        try {
            Page<Role> listRole = roleRepository.findAll(PageRequest.of(0, 10));
            return listRole.getContent().stream().map(x -> {
                Map<String, Object> mapData = new HashMap<>();
                mapData.put("id", x.getId());
                mapData.put("name", x.getName());
                return mapData;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Set<Role> getSignUpRole(Long roleId) {
        try {
            Set<Role> setRole = new HashSet<>();
            if (roleId == null) {
                Role userRole = roleRepository.findByName("ADMIN");
                if(userRole != null && userRole.getIsDeleted() == false){
                    setRole.add(userRole);
                }
            } else {
                Role userRole = roleRepository.findRoleById(roleId);
                if(userRole != null && userRole.getIsDeleted() == false){
                    setRole.add(userRole);
                }
            }
            return setRole;
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

}