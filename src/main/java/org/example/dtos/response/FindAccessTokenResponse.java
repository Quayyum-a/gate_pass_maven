package org.example.dtos.response;

import lombok.Data;
import org.example.data.models.AccessToken;

@Data
public class FindAccessTokenResponse {
    private String visitorName;
    private String visitorPhoneNumber;
    private String whomToSee;
    private String residentFullName;
    private String residentAddress;
    private String residentEmail;
    private boolean isValid;

    public AccessToken getAccessToken() {
        AccessToken accessToken = new AccessToken();
        accessToken.setVisitorName(visitorName);
        accessToken.setVisitorPhoneNumber(visitorPhoneNumber);
        accessToken.setWhomToSee(whomToSee);
        return accessToken;
    }

    public void setIsValid(boolean b) {
        isValid = b;
    }
}

