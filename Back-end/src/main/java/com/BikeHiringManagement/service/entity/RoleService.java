package com.BikeHiringManagement.service.entity;

import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.entity.Role;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Result createRole(String name){
        try{
            if(roleRepository.existsByName(name)){
                return new Result(Constant.LOGIC_ERROR_CODE, "The role name has been existed!!!");
            }else{
                Role newRole = new Role();
                newRole.setName(name);
                newRole.setCreatedUser("Tai Phuc");
                newRole.setCreatedDate(new Date());
                roleRepository.save(newRole);
                return new Result(Constant.SUCCESS_CODE, "Create new role successfully");
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }
}
