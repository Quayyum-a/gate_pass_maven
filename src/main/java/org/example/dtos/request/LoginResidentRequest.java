package org.example.dtos.request;

import lombok.Data;

@Data
public class LoginResidentRequest {
    private String email;
    private String password;

}
