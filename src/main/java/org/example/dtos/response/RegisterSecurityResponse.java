package org.example.dtos.response;

import lombok.Data;

@Data
public class RegisterSecurityResponse {
    private String message;
    private String fullName;
    private String phoneNumber;
    private String email;

    public RegisterSecurityResponse() {
        this.message = "Security registration successful";
    }
}
