package org.example.dtos.request;

import lombok.Data;

@Data
public class LoginResidentRequest {
    private String email;
    private String password;

    public LoginResidentRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}