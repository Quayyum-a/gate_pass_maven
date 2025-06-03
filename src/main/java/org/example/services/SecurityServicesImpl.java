package org.example.services;

import org.example.data.models.AccessToken;
import org.example.data.repositories.AccessTokens;
import org.example.data.repositories.Residents;
import org.example.data.repositories.Securities;
import org.example.dtos.request.FindAccessToken;
import org.example.dtos.request.LoginSecurityRequest;
import org.example.dtos.request.RegisterSecurityRequest;
import org.example.dtos.response.FindAccessTokenResponse;
import org.example.dtos.response.LoginSecurityResponse;
import org.example.dtos.response.RegisterSecurityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.example.verification.Verification.*;

@Service
public class SecurityServicesImpl implements SecurityServices {
    @Autowired
    private Securities securityRepository;

    @Autowired
    private Residents residentRepository;
    
    @Autowired
    private AccessTokens accessTokenRepository;
    @Autowired
    private Securities securities;

    @Override
    public RegisterSecurityResponse register(RegisterSecurityRequest request) {
        RegisterSecurityResponse response = new RegisterSecurityResponse();
        verifyEmail(request.getEmail());
        RegisterSecurityRequest requestRegister = new RegisterSecurityRequest();
        requestRegister.setFullName(request.getFullName());
        requestRegister.setEmail(request.getEmail());
        requestRegister.setPassword(request.getPassword());
        requestRegister.setPhoneNumber(request.getPhoneNumber());
        securities.save(requestRegister);
        response.setMessage("Security registered successfully");
        return response;
    }



    @Override
    public LoginSecurityResponse login(LoginSecurityRequest request) {
        verifyEmailAndPassword(request.getEmail(), request.getPassword());
        LoginSecurityResponse response = new LoginSecurityResponse();
        response.setMessage("Security logged in successfully");
        return response;
    }



    @Override
    public Object findAccessToken(FindAccessToken request) {
        FindAccessTokenResponse response = new FindAccessTokenResponse();
        AccessToken accessToken = accessTokenRepository.findByToken(request.getAccessCode());
        verifyToken(accessToken);
        response.setVisitorName(accessToken.getVisitorName());
        response.setVisitorPhoneNumber(accessToken.getVisitorPhoneNumber());
        response.setWhomToSee(accessToken.getWhomToSee());
        response.setResidentFullName(accessToken.getResident().getFullName());
        response.setResidentAddress(accessToken.getResident().getAddress());
        response.setResidentEmail(accessToken.getResident().getEmail());
        response.setIsValid(true);
        return response;
    }


}
