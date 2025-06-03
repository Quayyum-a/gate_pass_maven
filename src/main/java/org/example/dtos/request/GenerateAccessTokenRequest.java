package org.example.dtos.request;

import lombok.Data;

@Data
public class GenerateAccessTokenRequest {
    private String email;
    private String visitorName;
    private String visitorPhoneNumber;
    private String whomToSee;
}
