package com.swt.project.authService.service;

import com.swt.project.authService.entity.UserRole;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserService {

    public String userToJson(long id, String email, UserRole userRole){
        String jsonObject = "{\"id\":\""+id+"\",";
        jsonObject += "\"email\":\""+email+"\",";
        jsonObject += "\"userRole\":\""+userRole+"\"}";

        return jsonObject;
    }

}
