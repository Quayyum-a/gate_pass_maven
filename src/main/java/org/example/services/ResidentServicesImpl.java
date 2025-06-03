package org.example.services;


import org.example.data.models.AccessToken;
import org.example.data.models.Resident;
import org.example.data.repositories.AccessTokens;
import org.example.data.repositories.Residents;
import org.example.dtos.request.GenerateAccessTokenRequest;
import org.example.dtos.request.LoginResidentRequest;
import org.example.dtos.request.RegisterResidentRequest;
import org.example.dtos.response.LoginResidentResponse;
import org.example.dtos.response.RegisterResidentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.example.utils.Mapper.mapToAccessTokenResponse;

@Service
public class ResidentServicesImpl implements ResidentServices {
    @Autowired
    private Residents residentRepository;
    private AccessTokens accessTokenService;


    @Override
    public RegisterResidentResponse register(RegisterResidentRequest request) {
        Resident existingResident = residentRepository.findByEmail(request.getEmail());
        if (existingResident != null) {
            return null;
        }
        Resident savedResident = residentRepository.save(map(request));
        AccessToken accessToken = accessTokenService.generateToken(savedResident.getId());
        RegisterResidentResponse response = map(savedResident);
        response.setAccessToken(accessToken.getToken());
        return response;
    }

    @Override
    public LoginResidentResponse login(LoginResidentRequest request) {
        Resident resident = residentRepository.findByEmail(request.getEmail());
        if (resident == null || !resident.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return mapToLoginResponse(resident);
    }

    @Override
    public GenerateAccessTokenResponse generateAccessToken(GenerateAccessTokenRequest request) {
        Optional<Resident> residentOptional = residentRepository.findById(request.getResidentId());
        if (residentOptional.isEmpty()) {
            throw new IllegalArgumentException("Resident not found");
        }

        AccessToken accessToken = accessTokenService.generateTokenForVisitor(
            request.getResidentId(),
            request.getVisitorName(),
            request.getVisitorPhoneNumber()
        );

        return mapToAccessTokenResponse(accessToken);
    }
}
