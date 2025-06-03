package org.example.dtos.response;

import lombok.Data;

@Data
public class FindAccessTokenResponse {
    private String accessToken;
    private String visitorName;
    private String visitorPhoneNumber;
    private String whomToSee;
    private String residentFullName;
    private String residentEmail;
    private boolean isValid;
}
