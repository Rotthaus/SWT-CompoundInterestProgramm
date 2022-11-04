package com.swt.project.authService.controllers;

import com.swt.project.authService.entity.Users;
import com.swt.project.authService.repository.UserRepo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private UserRepo userRepo;


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


}