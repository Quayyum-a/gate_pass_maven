package org.example.dtos.response;

import lombok.Data;
import org.example.data.models.AccessToken;

import java.time.LocalDateTime;

@Data
public class FindAccessTokenResponse {
    private String visitorName;
    private String visitorPhoneNumber;
    private String whomToSee;
    private String residentFullName;
    private String residentAddress;
    private String residentEmail;
    private boolean isValid;
    private LocalDateTime expiryDate;


    public void setIsValid(boolean b) {
        isValid = b;
    }
}

