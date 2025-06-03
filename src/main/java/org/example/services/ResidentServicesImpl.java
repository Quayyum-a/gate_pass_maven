package org.example.services;


import org.example.data.models.AccessToken;
import org.example.data.models.Resident;
import org.example.data.models.Visitor;
import org.example.data.repositories.AccessTokens;
import org.example.data.repositories.Residents;
import org.example.data.repositories.Visitors;
import org.example.dtos.request.GenerateAccessTokenRequest;
import org.example.dtos.request.LoginResidentRequest;
import org.example.dtos.request.RegisterResidentRequest;
import org.example.dtos.response.GenerateAccessTokenResponse;
import org.example.dtos.response.LoginResidentResponse;
import org.example.dtos.response.RegisterResidentResponse;
import org.example.exceptions.GatePassException;
import org.example.exceptions.ResidentAlreadyExsitsException;
import org.example.exceptions.ResidentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.example.utils.Mapper.*;

@Service
public class ResidentServicesImpl implements ResidentServices {
    @Autowired
    private Residents residentRepository;
    @Autowired
    private AccessTokens accessTokenService;
    @Autowired
    private Visitors visitorRepository;


    @Override
    public RegisterResidentResponse register(RegisterResidentRequest request) {
        verifyEmail(request.getEmail());
        Resident resident = mapToRegisterResidentRequest(request);
        residentRepository.save(resident);
        return mapToRegisterResidentResponse(resident);
    }

    private void verifyEmail(String email) {
        if (residentRepository.findByEmail(email) != null) {
            throw new ResidentAlreadyExsitsException("Email already exists");
        }
    }

    @Override
    public LoginResidentResponse login(LoginResidentRequest request) {
        verifyEmailAndPassword(request.getEmail(), request.getPassword());
        Resident resident = residentRepository.findByEmail(request.getEmail());
        return mapToLoginResidentResponse(resident);
    }

    private void verifyEmailAndPassword(String email, String password) {
        Resident resident = residentRepository.findByEmail(email);
        if (resident == null || !resident.getPassword().equals(password)) {
            throw new GatePassException("Invalid email or password");
        }
    }

    @Override
    public GenerateAccessTokenResponse generateAccessToken(GenerateAccessTokenRequest request) {
        Resident resident = residentRepository.findByEmail(request.getEmail());
        if (resident == null) {
            throw new ResidentNotFoundException("Resident not found");
        }
        Visitor visitor = visitorInformation(request);
        Visitor savedVisitor = visitorRepository.save(visitor);
        AccessToken accessToken = accessTokenInformation(resident, savedVisitor);
        AccessToken savedToken = accessTokenService.save(accessToken);
        return mapToAccessTokenResponse(savedToken);
    }
}
