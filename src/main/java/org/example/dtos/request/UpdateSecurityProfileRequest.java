package org.example.dtos.request;

import lombok.Data;

@Data
public class UpdateSecurityProfileRequest {
    private String fullName;
    private String phoneNumber;
}