package org.example.utils;

import org.example.data.models.AccessToken;
import org.example.data.models.Resident;
import org.example.data.models.Security;
import org.example.data.models.Visitor;
import org.example.dtos.response.GenerateAccessTokenResponse;
import org.example.dtos.response.LoginResidentResponse;
import org.example.dtos.response.RegisterResidentResponse;
import org.example.dtos.response.RegisterSecurityResponse;
import org.example.dtos.request.GenerateAccessTokenRequest;
import org.example.dtos.request.RegisterResidentRequest;
import org.example.dtos.request.RegisterSecurityRequest;
import org.example.exceptions.AccessTokenNotFound;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class Mapper {
    public static Resident mapToRegisterResidentRequest(RegisterResidentRequest request) {
        Resident resident = new Resident();
        resident.setFullName(request.getFullName());
        resident.setAddress(request.getAddress());
        resident.setPhoneNumber(request.getPhoneNumber());
        resident.setEmail(request.getEmail());
        resident.setPassword(request.getPassword());
        return resident;
    }

    public static RegisterResidentResponse mapToRegisterResidentResponse(Resident resident) {
        RegisterResidentResponse response = new RegisterResidentResponse();
        response.setFullName(resident.getFullName());
        response.setAddress(resident.getAddress());
        response.setEmail(resident.getEmail());
        return response;
    }

    public static LoginResidentResponse mapToLoginResidentResponse(Resident resident) {
        LoginResidentResponse response = new LoginResidentResponse();
        response.setFullName(resident.getFullName());
        response.setEmail(resident.getEmail());
        return response;
    }

    public static Security mapToRegisterSecurityRequest(RegisterSecurityRequest request) {
        Security security = new Security();
        security.setFullName(request.getFullName());
        security.setPhoneNumber(request.getPhoneNumber());
        security.setEmail(request.getEmail());
        security.setPassword(request.getPassword());
        return security;
    }

    public static RegisterSecurityResponse mapToRegisterSecurityResponse(Security security) {
        RegisterSecurityResponse response = new RegisterSecurityResponse();
        response.setFullName(security.getFullName());
        response.setPhoneNumber(security.getPhoneNumber());
        response.setEmail(security.getEmail());
        response.setMessage("Security registered successfully");
        return response;
    }

    public static Visitor visitorInformation(GenerateAccessTokenRequest request) {
        Visitor visitor = new Visitor();
        visitor.setFullName(request.getVisitorName());
        visitor.setPhoneNumber(request.getVisitorPhoneNumber());
        visitor.setPurpose("Visit"); // Default purpose
        return visitor;
    }

    public static AccessToken accessTokenInformation(Resident resident, Visitor visitor) {
        AccessToken accessToken = new AccessToken();
        accessToken.setResident(resident);
        accessToken.setVisitor(visitor);
        accessToken.setCreationDate(LocalDateTime.now());
        accessToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        accessToken.setUsed(false);
        accessToken.setStatus("active");
        accessToken.setToken(generateAccessToken());
        return accessToken;
    }

    private static String generateAccessToken() {
        char[] chars = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
        };
        Random random = new Random();
        StringBuilder tokenBuilder = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(chars.length);
            tokenBuilder.append(chars[randomIndex]);
        }
        return tokenBuilder.toString();
    }

    public static GenerateAccessTokenResponse mapToAccessTokenResponse(AccessToken accessToken) {
        if (accessToken == null) {
            throw new AccessTokenNotFound("Access token cannot be null");
        }
        GenerateAccessTokenResponse response = new GenerateAccessTokenResponse();
        response.setToken(accessToken.getToken());
        response.setWhomToSee(accessToken.getWhomToSee());
        response.setExpiryDate(accessToken.getExpiryDate());
        response.setVisitorName(accessToken.getVisitorName());
        response.setVisitorPhoneNumber(accessToken.getVisitorPhoneNumber());
        response.setCreationDate(accessToken.getCreationDate());
        response.setUsed(accessToken.isUsed());
        return response;
    }
}