package org.example.dtos.response;

import lombok.Data;

@Data
public class UpdateSecurityProfileResponse {
    private String fullName;
    private String email;
    private String phoneNumber;
}