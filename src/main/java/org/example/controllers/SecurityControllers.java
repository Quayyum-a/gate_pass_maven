package org.example.controllers;

import org.example.dtos.request.FindAccessTokenRequest;
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

    private final SecurityServices securityServices;

    @Autowired
    public SecurityControllers(SecurityServices securityServices) {
        this.securityServices = securityServices;
    }

    @PostMapping("/security/register")
    public ResponseEntity<ApiResponse<?>> registerSecurity( @RequestBody RegisterSecurityRequest request) {
        try {
            RegisterSecurityResponse response = securityServices.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(response, "Security registration successful"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred during registration: " + e.getMessage(), false));
        }
    }

    @PostMapping("/security/login")
    public ResponseEntity<ApiResponse<?>> loginSecurity(
            @RequestParam("email") String email,
            @RequestParam("password") String password) {
        try {
            if (email == null || email.isBlank() || password == null || password.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Email and password are required", false));
            }
            LoginSecurityRequest loginRequest = new LoginSecurityRequest();
            loginRequest.setEmail(email.trim());
            loginRequest.setPassword(password);
            
            LoginSecurityResponse response = securityServices.login(loginRequest);
            return ResponseEntity.ok(new ApiResponse<>(response, "Login successful"));
        } catch (GatePassException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred during login: " + e.getMessage(), false));
        }
    }

    @PostMapping("/security/verify/token")
    public ResponseEntity<ApiResponse<?>> verifyAccessToken(@RequestBody FindAccessTokenRequest request) {
        try {
            if (request.getToken() == null || request.getToken().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Token is required", false));
            }
            FindAccessTokenResponse response = securityServices.findAccessToken(request);
            return ResponseEntity.ok(new ApiResponse<>(response, "Token verification successful"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred during token verification: " + e.getMessage(), false));
        }
    }
}
