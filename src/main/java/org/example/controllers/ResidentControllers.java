package org.example.controllers;

import org.example.dtos.request.*;
import org.example.dtos.response.*;
import org.example.exceptions.GatePassException;
import org.example.services.ResidentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class ResidentControllers {

    private final ResidentServices residentServices;

    @Autowired
    public ResidentControllers(ResidentServices residentServices) {
        this.residentServices = residentServices;
    }

    @PostMapping("/resident/register")
    public ResponseEntity<ApiResponse<?>> registerResident( @RequestBody RegisterResidentRequest request) {
        try {
            if (request == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Request body cannot be null", false));
            }
            RegisterResidentResponse response = residentServices.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(response, "Resident registered successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred during registration: " + e.getMessage(), false));
        }
    }

    @PostMapping("/resident/login")
    public ResponseEntity<ApiResponse<?>> loginResident(
            @RequestParam String email,
            @RequestParam String password) {
        try {
            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Email and password are required", false));
            }
            LoginResidentRequest loginRequest = new LoginResidentRequest(email.trim(), password.trim());
            LoginResidentResponse response = residentServices.login(loginRequest);
            return ResponseEntity.ok(new ApiResponse<>(response, "Login successful"));
        } catch (GatePassException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred during login", false));
        }
    }

    @PostMapping("/resident/generate/code")
    public ResponseEntity<ApiResponse<?>> generateAccessToken( @RequestBody GenerateAccessTokenRequest request) {
        try {
            if (request == null || request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Email is required", false));
            }
            // Trim email to remove any accidental whitespace
            request.setEmail(request.getEmail().trim());
            GenerateAccessTokenResponse response = residentServices.generateAccessToken(request);
            return ResponseEntity.ok(new ApiResponse<>(response, "Access token generated successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while generating access token", false));
        }
    }

    @PostMapping("/resident/find/code")
    public ResponseEntity<ApiResponse<?>> findAccessToken( @RequestBody FindAccessTokenRequest request) {
        try {
            if (request == null || request.getToken() == null || request.getToken().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Token is required", false));
            }
            // Trim token to remove any accidental whitespace
            request.setToken(request.getToken().trim());
            FindAccessTokenResponse response = residentServices.findAccessToken(request);
            return ResponseEntity.ok(new ApiResponse<>(response, "Token found"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while finding access token", false));
        }
    }
}
