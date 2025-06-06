package org.example.controllers;


import org.example.dtos.request.*;
import org.example.dtos.response.*;
import org.example.exceptions.GatePassException;
import org.example.services.SecurityServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping("/api")
public class SecurityControllers {

    private final SecurityServices securityServices;

    @Autowired
    public SecurityControllers(SecurityServices securityServices) {
        this.securityServices = securityServices;
    }

    @PostMapping("/security/register")
    public ResponseEntity<ApiResponse<?>> registerSecurity(@RequestBody RegisterSecurityRequest request) {
        try {
            RegisterSecurityResponse response = securityServices.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(new UserResponse(response), "Security registration successful"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred during registration: " + e.getMessage(), false));
        }
    }

    @PostMapping("/security/login")
    public ResponseEntity<ApiResponse<?>> loginSecurity(@RequestBody LoginSecurityRequest request) {
        try {
            if (request == null || request.getEmail() == null || request.getPassword() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Email and password are required", false));
            }
            LoginSecurityResponse response = securityServices.login(request);
            return ResponseEntity.ok(new ApiResponse<>(new UserResponse(response), "Login successful"));
        } catch (GatePassException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred during login: " + e.getMessage(), false));
        }
    }

    @PostMapping("/security/verify/token")
    public ResponseEntity<ApiResponse<?>> verifyAccessToken(@RequestHeader("Authorization") String token, @RequestBody FindAccessTokensRequest request) {
        try {
            if (request.getToken() == null || request.getToken().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Token is required", false));
            }
            FindAccessTokenResponse response = securityServices.findAccessToken(request, token);
            return ResponseEntity.ok(new ApiResponse<>(response, "Token verification successful"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred during token verification: " + e.getMessage(), false));
        }
    }

    @PostMapping("/security/visitor/logs")
    public ResponseEntity<ApiResponse<?>> getVisitorLogs(@RequestHeader("Authorization") String token, @RequestBody VisitorLogsRequest request) {
        try {
            List<VisitorLogResponse> response = securityServices.getVisitorLogs(request, token);
            return ResponseEntity.ok(new ApiResponse<>(new LogsResponse(response), "Visitor logs retrieved successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while retrieving visitor logs", false));
        }
    }

    @PostMapping("/security/profile")
    public ResponseEntity<ApiResponse<?>> updateProfile(@RequestHeader("Authorization") String token, @RequestBody UpdateSecurityProfileRequest request) {
        try {
            UpdateSecurityProfileResponse response = securityServices.updateProfile(request, token);
            return ResponseEntity.ok(new ApiResponse<>(new UserResponse(response), "Profile updated successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while updating profile", false));
        }
    }

    @PostMapping("/security/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(@RequestHeader("Authorization") String token, @RequestBody ChangePasswordRequest request) {
        try {
            securityServices.changePassword(request, token);
            return ResponseEntity.ok(new ApiResponse<>(null, "Password changed successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while changing password", false));
        }
    }

    @PostMapping("/security/upload-profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = securityServices.uploadProfilePicture(file, token);
            return ResponseEntity.ok(new ApiResponse<>(fileUrl, "Profile picture uploaded successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while uploading profile picture", false));
        }
    }

    @GetMapping("/security/dashboard-stats")
    public ResponseEntity<ApiResponse<?>> getDashboardStats(@RequestHeader("Authorization") String token) {
        try {
            DashboardStatsResponse response = securityServices.getDashboardStats(token);
            return ResponseEntity.ok(new ApiResponse<>(response, "Dashboard stats retrieved successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while retrieving dashboard stats", false));
        }
    }
}