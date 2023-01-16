package com.swt.project.authService.controllers;

import com.swt.project.authService.entity.RefreshToken;
import com.swt.project.authService.models.dataAccessObject.TokenRefreshRequestDAO;
import com.swt.project.authService.models.dataTransferObject.AuthResponseDTO;
import com.swt.project.authService.entity.UserRole;
import com.swt.project.authService.entity.Users;
import com.swt.project.authService.models.LoginCredentials;
import com.swt.project.authService.models.dataTransferObject.TokenRefreshResponseDTO;
import com.swt.project.authService.repository.UserRepo;
import com.swt.project.authService.security.JWTUtil;
import com.swt.project.authService.service.RefreshTokenService;
import com.swt.project.authService.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepo userRepo;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserService userService;
    @Autowired private RefreshTokenService refreshTokenService;


    /**
     * adds a user to the database
     *
     * @return ResponseEntity
     */
    @PostMapping(value = "/register")
    @ApiOperation(value = "adds a user to the database")
    public ResponseEntity<?> registerHandler(@RequestBody Users user){

        if(userService.isValidEmail(user.getEmail())){
            if(userService.isValidPassword(user.getPassword())) {
                if (userRepo.existsByEmail(user.getEmail())) {
                    return new ResponseEntity<>("E-mail already exist", HttpStatus.BAD_REQUEST);
                }
                try {
                    String encodedPass = passwordEncoder.encode(user.getPassword());
                    user.setPassword(encodedPass);
                    user.setUserRole(UserRole.USER);
                    user = userRepo.save(user);
                    String token = jwtUtil.generateToken(user.getEmail());
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
                    return new ResponseEntity<>(new AuthResponseDTO(token, refreshToken.getToken()), HttpStatus.OK);
                    // return ResponseEntity.ok(Collections.singletonMap("jwt-token", token));
                } catch (Exception e) {
                    return new ResponseEntity<>("Register failed.", HttpStatus.CONFLICT);
                }
            }else{
                return new ResponseEntity<>("Invalid Password", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Invalid E-Mail", HttpStatus.BAD_REQUEST);
    }

    /**
     * returns an access token for a specific user from server
     *
     * @return ResponseEntity
     */
    @PostMapping("/login")
    @ApiOperation(value = "returns an access token for a specific user from server")
    public  ResponseEntity<?> loginHandler(@RequestBody LoginCredentials body){
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(
                            body.getEmail(),
                            body.getPassword());
            authManager.authenticate(authInputToken);
            String token = jwtUtil.generateToken(body.getEmail());

            Optional<Users> user = userRepo.findByEmail(body.getEmail());
            if (user.isPresent()) {
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.get().getId());
                return new ResponseEntity<>(new AuthResponseDTO(token, refreshToken.getToken()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("user is not present", HttpStatus.BAD_REQUEST);
            }
           // return ResponseEntity.ok(Collections.singletonMap("jwt-token", token));
        }catch (AuthenticationException authExc){
            return new ResponseEntity<>("Invalid Login Credentials", HttpStatus.BAD_REQUEST);
        }
    }
    /**
    * returns an access token with the refresh token from user
    *
     */
    @PostMapping("refreshtoken")
    public ResponseEntity<?> refreshtoken( @RequestBody TokenRefreshRequestDAO request) {
        String requestRefreshToken = request.getRefreshToken();
        String token ;

        Optional<RefreshToken> refreshToken = refreshTokenService.findByToken(requestRefreshToken);
        if(refreshToken.isPresent()){
            token = jwtUtil.generateToken(refreshToken.get().getUser().getEmail());
            return ResponseEntity.ok(new TokenRefreshResponseDTO(token, requestRefreshToken));
        } else {
        return new ResponseEntity<>("Token not in database", HttpStatus.BAD_REQUEST);
        }

        /*return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponseDTO(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!")); */
    }
}
