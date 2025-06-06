package org.example.dtos.response;

import lombok.Data;

@Data
public class LoginSecurityResponse {
    private String message;
    private String fullName;
    private String email;
    private String phoneNumber;
}
