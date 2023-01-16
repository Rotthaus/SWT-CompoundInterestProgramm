package com.swt.project.authService.service;

import com.swt.project.authService.entity.UserRole;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
public class UserService {

    public String userToJson(long id, String email, UserRole userRole){
        String jsonObject = "{\"id\":\""+id+"\",";
        jsonObject += "\"email\":\""+email+"\",";
        jsonObject += "\"userRole\":\""+userRole+"\"}";

        return jsonObject;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        Pattern pat = Pattern.compile(passwordRegex);
        if (password == null)
            return false;
        return pat.matcher(password).matches();
    }
}
