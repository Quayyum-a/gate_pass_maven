package org.example.controllers;

import org.example.dtos.request.FindAccessToken;
import org.example.dtos.request.LoginSecurityRequest;
import org.example.dtos.request.RegisterSecurityRequest;
import org.example.dtos.response.*;
import org.example.exceptions.GatePassException;
import org.example.services.SecurityServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class SecurityControllers {

    @Autowired
    private SecurityServices securityServices;

    @PostMapping("/security/register")
    public ResponseEntity<?> registerSecurity(@RequestBody RegisterSecurityRequest request) {
        try {
            RegisterSecurityResponse response = securityServices.register(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        } catch (GatePassException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/security/login")
    public ResponseEntity<?> loginSecurity(@RequestParam String email, @RequestParam String password) {
        try {
            LoginSecurityRequest loginRequest = new LoginSecurityRequest();
            loginRequest.setEmail(email);
            loginRequest.setPassword(password);
            LoginSecurityResponse response = securityServices.login(loginRequest);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
        } catch (GatePassException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/security/verify/token")
    public ResponseEntity<?> verifyAccessToken(@RequestBody FindAccessToken request) {
        try {
            Object response = securityServices.findAccessToken(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
        } catch (GatePassException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }
}
