package org.example.dtos.request;

import lombok.Data;

@Data
public class RegisterSecurityRequest {
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
}
