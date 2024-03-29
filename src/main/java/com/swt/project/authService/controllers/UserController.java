package com.swt.project.authService.controllers;

import com.swt.project.authService.entity.UserRole;
import com.swt.project.authService.entity.Users;
import com.swt.project.authService.models.dataAccessObject.ChangePasswordRequestDAO;
import com.swt.project.authService.repository.UserRepo;
import com.swt.project.authService.service.RefreshTokenService;
import com.swt.project.authService.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired private UserRepo userRepo;
    @Autowired private UserService userService;
    @Autowired private AuthenticationManager authManager;

    @Autowired private RefreshTokenService refreshTokenService;

    @Autowired private PasswordEncoder passwordEncoder;

    /**
     * returns details for a specific user
     *
     * @return Users
     */
    @GetMapping("/info")
    @ApiOperation(value = "returns details for a specific user")
    public ResponseEntity getUserDetails(){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = userRepo.findByEmail(email).get().getId();
        String userEmail = userRepo.findByEmail(email).get().getEmail();
        UserRole userRole = userRepo.findByEmail(email).get().getUserRole();

        return new ResponseEntity(userService.userToJson(id, userEmail, userRole), HttpStatus.OK);
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
            Long id = userRepo.findByEmail(email).get().getId();
            refreshTokenService.deleteByUserId(id);
            userRepo.deleteById(id);
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        }catch(Exception e){
            log.info(e.getMessage());
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