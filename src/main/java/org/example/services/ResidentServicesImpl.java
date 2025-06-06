package org.example.services;

import org.example.data.models.AccessToken;
import org.example.data.models.Resident;
import org.example.data.models.Visitor;
import org.example.data.repositories.AccessTokens;
import org.example.data.repositories.Residents;
import org.example.data.repositories.Visitors;
import org.example.dtos.request.FindAccessTokenRequest;
import org.example.dtos.request.GenerateAccessTokenRequest;
import org.example.dtos.request.LoginResidentRequest;
import org.example.dtos.request.RegisterResidentRequest;
import org.example.dtos.response.FindAccessTokenResponse;
import org.example.dtos.response.GenerateAccessTokenResponse;
import org.example.dtos.response.LoginResidentResponse;
import org.example.dtos.response.RegisterResidentResponse;
import org.example.exceptions.GatePassException;
import org.example.exceptions.ResidentAlreadyExsitsException;
import org.example.exceptions.ResidentNotFoundException;
import org.example.verification.VerificationNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.example.utils.Mapper.*;

@Service
public class ResidentServicesImpl implements ResidentServices {
    private final Residents residentRepository;
    private final AccessTokens accessTokenRepository;
    private final Visitors visitorRepository;

    @Autowired
    public ResidentServicesImpl(
            Residents residentRepository,
            AccessTokens accessTokenRepository,
            Visitors visitorRepository) {
        this.residentRepository = residentRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.visitorRepository = visitorRepository;
    }

    @Override
    @Transactional
    public RegisterResidentResponse register(RegisterResidentRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Invalid registration request");
        }
        
        if (residentRepository.findByEmail(request.getEmail()) != null) {
            throw new ResidentAlreadyExsitsException("Email already registered");
        }
        
        Resident resident = mapToRegisterResidentRequest(request);
        Resident savedResident = residentRepository.save(resident);
        return mapToRegisterResidentResponse(savedResident);
    }

    @Override
    public LoginResidentResponse login(LoginResidentRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Email and password are required");
        }
        
        Resident resident = residentRepository.findByEmail(request.getEmail());
        if (resident == null || !resident.getPassword().equals(request.getPassword())) {
            throw new GatePassException("Invalid email or password");
        }
        
        return mapToLoginResidentResponse(resident);
    }

    @Override
    @Transactional
    public GenerateAccessTokenResponse generateAccessToken(GenerateAccessTokenRequest request) {
        if (request == null || request.getEmail() == null) {
            throw new IllegalArgumentException("Invalid token generation request");
        }
        
        Resident resident = residentRepository.findByEmail(request.getEmail());
        if (resident == null) {
            throw new ResidentNotFoundException("Resident not found");
        }

        Visitor visitor = visitorInformation(request);
        Visitor savedVisitor = visitorRepository.save(visitor);
        
        // Create and save access token
        AccessToken accessToken = accessTokenInformation(resident, savedVisitor);
        accessToken.setVisitorName(savedVisitor.getFullName());
        accessToken.setVisitorPhoneNumber(savedVisitor.getPhoneNumber());
        accessToken.setWhomToSee(resident.getFullName());
        accessToken.setCreationDate(LocalDateTime.now());
        accessToken.setExpiryDate(LocalDateTime.now().plusMinutes(30)); // 30 minutes expiry
        accessToken.setUsed(false);
        
        AccessToken savedToken = accessTokenRepository.save(accessToken);
        return mapToAccessTokenResponse(savedToken);
    }

    @Override
    public FindAccessTokenResponse findAccessToken(FindAccessTokenRequest request) {
        if (request == null || request.getToken() == null) {
            throw new IllegalArgumentException("Token is required");
        }
        return getFindAccessTokenResponse(request, accessTokenRepository);
    }

    static FindAccessTokenResponse getFindAccessTokenResponse(FindAccessTokenRequest request, AccessTokens accessTokenRepository) {
        AccessToken accessToken = VerificationNew.verifyToken(request.getToken());

        accessToken.setUsed(true);
        accessTokenRepository.save(accessToken);

        FindAccessTokenResponse response = new FindAccessTokenResponse();
        response.setVisitorName(accessToken.getVisitorName());
        response.setVisitorPhoneNumber(accessToken.getVisitorPhoneNumber());
        response.setWhomToSee(accessToken.getWhomToSee());
        response.setResidentFullName(accessToken.getResident().getFullName());
        response.setResidentAddress(accessToken.getResident().getAddress());
        response.setResidentEmail(accessToken.getResident().getEmail());
        response.setIsValid(true);
        response.setExpiryDate(accessToken.getExpiryDate());
        return response;
    }
}
