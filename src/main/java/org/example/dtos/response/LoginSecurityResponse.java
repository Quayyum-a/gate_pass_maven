package org.example.dtos.response;

import lombok.Data;

@Data
public class LoginSecurityResponse {
    private String email;
    private String fullName;
}
