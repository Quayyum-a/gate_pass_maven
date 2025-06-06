package org.example.dtos.response;

import lombok.Data;

@Data
public class UpdateResidentProfileResponse {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
}