package org.example.services;

import org.example.dtos.request.*;
import org.example.dtos.response.*;

/**
 * Service interface for resident-related operations
 */
public interface ResidentServices {
    /**
     * Register a new resident
     */
    RegisterResidentResponse register(RegisterResidentRequest request);
    
    /**
     * Authenticate a resident
     */
    LoginResidentResponse login(LoginResidentRequest request);
    
    /**
     * Generate an access token for a visitor
     */
    GenerateAccessTokenResponse generateAccessToken(GenerateAccessTokenRequest request);

    /**
     * Find and validate an access token
     */
    FindAccessTokenResponse findAccessToken(FindAccessTokenRequest request);
}
