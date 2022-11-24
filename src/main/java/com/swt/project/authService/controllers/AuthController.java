package com.swt.project.authService.controllers;

import com.swt.project.authService.entity.UserRole;
import com.swt.project.authService.entity.Users;
import com.swt.project.authService.models.LoginCredentials;
import com.swt.project.authService.repository.UserRepo;
import com.swt.project.authService.security.JWTUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepo userRepo;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;


    /**
     * adds a user to the database
     *
     * @return ResponseEntity
     */
    @PostMapping(value = "/register")
    @ApiOperation(value = "adds a user to the database")
    public ResponseEntity<?> registerHandler(@RequestBody Users user){
        if(userRepo.existsByEmail(user.getEmail())){
            return new ResponseEntity<>("E-mail already exist", HttpStatus.BAD_REQUEST);
        }
        try {
            String encodedPass = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPass);
            user.setUserRole(UserRole.USER);
            user = userRepo.save(user);
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(Collections.singletonMap("jwt-token", token));
        } catch (Exception e){
            return new ResponseEntity<>("Register failed.", HttpStatus.CONFLICT);
        }
    }

    /**
     * returns an access token for a specific user from server
     *
     * @return ResponseEntity
     */
    @PostMapping("/login")
    @ApiOperation(value = "returns an access token for a specific user from server")
    public  ResponseEntity<?> loginHandler(@RequestBody LoginCredentials body){
        log.info(body.getEmail());
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());

            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(body.getEmail());

            return ResponseEntity.ok(Collections.singletonMap("jwt-token", token));
        }catch (AuthenticationException authExc){
            return new ResponseEntity<>("Invalid Login Credentials", HttpStatus.BAD_REQUEST);
        }
    }
}