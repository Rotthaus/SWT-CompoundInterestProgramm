package com.swt.project.authService.controllers;

import com.swt.project.authService.entity.Users;
import com.swt.project.authService.models.dataAccessObject.ChangePasswordRequestDAO;
import com.swt.project.authService.repository.UserRepo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private UserRepo userRepo;
    @Autowired private AuthenticationManager authManager;

    @Autowired private PasswordEncoder passwordEncoder;

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
     * @return ResponseEntity<String>
     */
    @PostMapping("/deleteUser")
    @ApiOperation(value = "deletes a user")
    public ResponseEntity<String> deleteUser(@RequestBody String password){
        try {
            String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(email, password);

            authManager.authenticate(authInputToken).isAuthenticated();
            userRepo.deleteById(userRepo.findByEmail(email).get().getId());
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("permission denied", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/changePassword")
    @ApiOperation(value = "change password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequestDAO changePasswordRequestDAO) {
        try {
            String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(email, changePasswordRequestDAO.getOldPassword());
            authManager.authenticate(authInputToken).isAuthenticated();
            Optional<Users> user = userRepo.findByEmail(email);
            if (user.isPresent()) {
                user.get().setPassword(passwordEncoder.encode(changePasswordRequestDAO.getNewPassword()));
                userRepo.save(user.get());
                return new ResponseEntity<>("password changed", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("user is not present", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            return new ResponseEntity<>("permission denied", HttpStatus.FORBIDDEN);
        }
    }

}