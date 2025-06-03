package org.example.dtos.request;

import lombok.Data;

@Data
public class LoginSecurityRequest {
    private String email;
    private String password;
}
