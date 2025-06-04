package org.example.services;

import org.example.dtos.request.FindAccessToken;
import org.example.dtos.request.GenerateAccessTokenRequest;
import org.example.dtos.request.LoginResidentRequest;
import org.example.dtos.request.RegisterResidentRequest;
import org.example.dtos.response.FindAccessTokenResponse;
import org.example.dtos.response.GenerateAccessTokenResponse;
import org.example.dtos.response.LoginResidentResponse;
import org.example.dtos.response.RegisterResidentResponse;

public interface ResidentServices {
    RegisterResidentResponse register(RegisterResidentRequest request);
    LoginResidentResponse login(LoginResidentRequest request);
    GenerateAccessTokenResponse generateAccessToken(GenerateAccessTokenRequest request);

    FindAccessTokenResponse findAccessToken(FindAccessToken residentRequest);
}
