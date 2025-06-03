package org.example.dtos.request;

import lombok.Data;

@Data
public class RegisterResidentRequest {
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String password;


}
