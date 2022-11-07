package com.swt.project.authService.controllers;

import com.swt.project.authService.entity.Users;
import com.swt.project.authService.models.LoginCredentials;
import com.swt.project.authService.repository.UserRepo;
import io.swagger.annotations.ApiOperation;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private UserRepo userRepo;
    @Autowired private AuthenticationManager authManager;

    /**
     * returns details for a specific user
     *
     * @return Users
     */
    @GetMapping("/info")
    @ApiOperation(value = "returns details for a specific user")
    public Users getUserDetails(){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByEmail(email).get();
    }


    /**
     * deletes a user with all data records from the server
     *
     * @return String
     */
    @PostMapping("/deleteUser")
    @ApiOperation(value = "deletes a user")
    public String deleteUser(@ModelAttribute String password){
        try {
            String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(email, password);

            authManager.authenticate(authInputToken).isAuthenticated();
            userRepo.deleteById(userRepo.findByEmail(email).get().getId());

            return "User deleted";
        }catch(Exception e){
            return "permission denied";
        }
    }

}