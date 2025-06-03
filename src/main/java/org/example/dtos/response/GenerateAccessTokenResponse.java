package org.example.dtos.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GenerateAccessTokenResponse {
    private String email;
    private String token;
    private LocalDateTime expiryDate;
    private String visitorName;
    private String visitorPhoneNumber;
    private String whomToSee;

}