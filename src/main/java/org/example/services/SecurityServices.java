package org.example.services;

import org.example.dtos.request.FindAccessTokenRequest;
import org.example.dtos.request.LoginSecurityRequest;
import org.example.dtos.request.RegisterSecurityRequest;
import org.example.dtos.response.FindAccessTokenResponse;
import org.example.dtos.response.LoginSecurityResponse;
import org.example.dtos.response.RegisterSecurityResponse;

/**
 * Service interface for security personnel operations
 */
public interface SecurityServices {
    /**
     * Register a new security personnel
     */
    RegisterSecurityResponse register(RegisterSecurityRequest request);

    /**
     * Authenticate a security personnel
     */
    LoginSecurityResponse login(LoginSecurityRequest request);

    /**
     * Find and validate an access token
     */
    FindAccessTokenResponse findAccessToken(FindAccessTokenRequest request);
}
