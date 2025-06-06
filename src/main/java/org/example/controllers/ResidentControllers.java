package org.example.controllers;



import org.example.dtos.request.*;
import org.example.dtos.response.*;
import org.example.exceptions.GatePassException;
import org.example.services.ResidentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping("/api")
public class ResidentControllers {

    private final ResidentServices residentServices;

    @Autowired
    public ResidentControllers(ResidentServices residentServices) {
        this.residentServices = residentServices;
    }

    @PostMapping("/resident/register")
    public ResponseEntity<ApiResponse<?>> registerResident(@RequestBody RegisterResidentRequest request) {
        try {
            if (request == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Request body cannot be null", false));
            }
            RegisterResidentResponse response = residentServices.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(new UserResponse(response), "Resident registered successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred during registration: " + e.getMessage(), false));
        }
    }

    @PostMapping("/resident/login")
    public ResponseEntity<ApiResponse<?>> loginResident(@RequestBody LoginResidentRequest request) {
        try {
            if (request == null || request.getEmail() == null || request.getPassword() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Email and password are required", false));
            }
            LoginResidentResponse response = residentServices.login(request);
            return ResponseEntity.ok(new ApiResponse<>(new UserResponse(response), "Login successful"));
        } catch (GatePassException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred during login", false));
        }
    }

    @PostMapping("/resident/generate/code")
    public ResponseEntity<ApiResponse<?>> generateAccessToken(@RequestHeader("Authorization") String token, @RequestBody GenerateAccessTokenRequest request) {
        try {
            if (request == null || request.getVisitorName() == null || request.getVisitorPhoneNumber() == null || request.getWhomToSee() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Required fields are missing", false));
            }
            GenerateAccessTokenResponse response = residentServices.generateAccessToken(request, token);
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
    public ResponseEntity<ApiResponse<?>> findAccessTokens(@RequestHeader("Authorization") String token, @RequestBody FindAccessTokensRequest request) {
        try {
            List<FindAccessTokenResponse> response = residentServices.findAccessTokens(request, token);
            return ResponseEntity.ok(new ApiResponse<>(new TokensResponse(response), "Tokens retrieved successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while retrieving tokens", false));
        }
    }

    @PostMapping("/resident/revoke/code")
    public ResponseEntity<ApiResponse<?>> revokeAccessToken(@RequestHeader("Authorization") String token, @RequestBody RevokeAccessTokenRequest request) {
        try {
            residentServices.revokeAccessToken(request, token);
            return ResponseEntity.ok(new ApiResponse<>(null, "Token revoked successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while revoking token", false));
        }
    }

    @PostMapping("/resident/profile")
    public ResponseEntity<ApiResponse<?>> updateProfile(@RequestHeader("Authorization") String token, @RequestBody UpdateResidentProfileRequest request) {
        try {
            UpdateResidentProfileResponse response = residentServices.updateProfile(request, token);
            return ResponseEntity.ok(new ApiResponse<>(new UserResponse(response), "Profile updated successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while updating profile", false));
        }
    }

    @PostMapping("/resident/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(@RequestHeader("Authorization") String token, @RequestBody ChangePasswordRequest request) {
        try {
            residentServices.changePassword(request, token);
            return ResponseEntity.ok(new ApiResponse<>(null, "Password changed successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while changing password", false));
        }
    }

    @PostMapping("/resident/upload-profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = residentServices.uploadProfilePicture(file, token);
            return ResponseEntity.ok(new ApiResponse<>(fileUrl, "Profile picture uploaded successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while uploading profile picture", false));
        }
    }

    @PostMapping("/resident/visitor/history")
    public ResponseEntity<ApiResponse<?>> getVisitorHistory(@RequestHeader("Authorization") String token, @RequestBody VisitorHistoryRequest request) {
        try {
            List<VisitorHistoryResponse> response = residentServices.getVisitorHistory(request, token);
            return ResponseEntity.ok(new ApiResponse<>(new VisitorsResponse(response), "Visitor history retrieved successfully"));
        } catch (GatePassException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An error occurred while retrieving visitor history", false));
        }
    }

    @GetMapping("/resident/dashboard-stats")
    public ResponseEntity<ApiResponse<?>> getDashboardStats(@RequestHeader("Authorization") String token) {
        try {
            DashboardStatsResponse response = residentServices.getDashboardStats(token);
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