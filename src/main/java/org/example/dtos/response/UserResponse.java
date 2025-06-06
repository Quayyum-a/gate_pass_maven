package org.example.dtos.response;

import lombok.Data;

@Data
public class UserResponse {
    private String fullName;
    private String email;
    private String phone;
    private String address; // Optional for security
    private String token;

    public UserResponse(RegisterResidentResponse response) {
        this.fullName = response.getFullName();
        this.email = response.getEmail();
        this.address = response.getAddress();
        this.token = generateToken();
    }

    public UserResponse(LoginResidentResponse response) {
        this.fullName = response.getFullName();
        this.email = response.getEmail();
        this.token = generateToken();
    }

    
    public UserResponse(LoginSecurityResponse response) {
        this.fullName = response.getFullName();
        this.email = response.getEmail();
        this.phone = response.getPhoneNumber();
        this.token = generateToken();
    }


    public UserResponse(RegisterSecurityResponse response) {
        this.fullName = response.getFullName();
        this.email = response.getEmail();
        this.phone = response.getPhoneNumber();
        this.token = generateToken();
    }

    public UserResponse(UpdateResidentProfileResponse response) {
        this.fullName = response.getFullName();
        this.email = response.getEmail();
        this.phone = response.getPhoneNumber();
        this.address = response.getAddress();
        this.token = generateToken();
    }

    public UserResponse(UpdateSecurityProfileResponse response) {
        this.fullName = response.getFullName();
        this.email = response.getEmail();
        this.phone = response.getPhoneNumber();
        this.token = generateToken();
    }

    private String generateToken() {
        // Simplified JWT generation (in production, use a proper JWT library)
        return "jwt-" + System.currentTimeMillis();
    }
}
