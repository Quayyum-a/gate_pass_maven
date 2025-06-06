package org.example.controllers;

import org.example.dtos.request.*;
import org.example.dtos.response.*;
import org.example.exceptions.GatePassException;
import org.example.services.ResidentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<ApiResponse<?>> registerResident(@Valid @RequestBody RegisterResidentRequest request) {
        try {
            RegisterResidentResponse response = residentServices.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(response, "Resident registered successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred during registration", false));
        }
    }

    @PostMapping("/resident/login")
    public ResponseEntity<ApiResponse<?>> loginResident(
            @RequestParam String email,
            @RequestParam String password) {
        try {
            if (email == null || email.trim().isEmpty() || password == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Email and password are required", false));
            }
            LoginResidentResponse response = residentServices.login(new LoginResidentRequest(email, password));
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
    public ResponseEntity<ApiResponse<?>> generateAccessToken(@Valid @RequestBody GenerateAccessTokenRequest request) {
        try {
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
    public ResponseEntity<ApiResponse<?>> findAccessToken(@Valid @RequestBody FindAccessTokenRequest request) {
        try {
            if (request.getToken() == null || request.getToken().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Token is required", false));
            }
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
