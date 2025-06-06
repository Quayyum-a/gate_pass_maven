package org.example.services;

import org.example.data.models.Security;
import org.example.data.repositories.AccessTokens;
import org.example.data.repositories.Securities;
import org.example.dtos.request.FindAccessTokenRequest;
import org.example.dtos.request.LoginSecurityRequest;
import org.example.dtos.request.RegisterSecurityRequest;
import org.example.dtos.response.FindAccessTokenResponse;
import org.example.dtos.response.LoginSecurityResponse;
import org.example.dtos.response.RegisterSecurityResponse;
import org.example.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.example.services.ResidentServicesImpl.getFindAccessTokenResponse;
import static org.example.verification.VerificationNew.*;

@Service
public class SecurityServicesImpl implements SecurityServices {
    private final Securities securityRepository;
    private final AccessTokens accessTokenRepository;

    @Autowired
    public SecurityServicesImpl(Securities securityRepository, AccessTokens accessTokenRepository) {
        this.securityRepository = securityRepository;
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public RegisterSecurityResponse register(RegisterSecurityRequest request) {
        verifyEmailAndPassword(request.getEmail(), request.getPassword());
        verifyEmail(request.getEmail());

        Security security = Mapper.mapToRegisterSecurityRequest(request);
        Security savedSecurity = securityRepository.save(security);

        RegisterSecurityResponse response = new RegisterSecurityResponse();
        response.setFullName(savedSecurity.getFullName());
        response.setEmail(savedSecurity.getEmail());
        response.setPhoneNumber(savedSecurity.getPhoneNumber());
        response.setMessage("Security registered successfully");
        return response;
    }

    @Override
    public LoginSecurityResponse login(LoginSecurityRequest request) {
        verifyEmailAndPassword(request.getEmail(), request.getPassword());
        verifyEmail(request.getEmail());

        LoginSecurityResponse response = new LoginSecurityResponse();
        response.setMessage("Login successful");
        return response;
    }

    @Override
    public FindAccessTokenResponse findAccessToken(FindAccessTokenRequest request) {
        verifyToken(request.getToken());
        return getFindAccessTokenResponse(request, accessTokenRepository);
    }
}
