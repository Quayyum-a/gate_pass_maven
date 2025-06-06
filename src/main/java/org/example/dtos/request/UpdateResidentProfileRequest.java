package org.example.dtos.request;

import lombok.Data;

@Data
public class UpdateResidentProfileRequest {
    private String fullName;
    private String phoneNumber;
    private String address;
}