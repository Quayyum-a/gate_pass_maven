package org.example.verification;

import org.example.data.models.AccessToken;
import org.example.data.repositories.AccessTokens;
import org.example.data.repositories.Securities;
import org.example.exceptions.AccessTokenNotFound;
import org.example.exceptions.SecurityAlreadyExsistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;

@Component
public class VerificationNew {
    private static VerificationNew instance;

    @Autowired
    private Securities securities;

    @Autowired
    private AccessTokens accessTokenRepository;

    @PostConstruct
    public void init() {
        instance = this;
    }

    public static void verifyEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (instance.securities.findByEmail(email) != null) {
            throw new SecurityAlreadyExsistException("Email already exists");
        }
    }

    public static void verifyEmailAndPassword(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password are required");
        }
    }

    public static AccessToken verifyToken(String tokenValue) {
        if (tokenValue == null || tokenValue.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        AccessToken accessToken = instance.accessTokenRepository.findByToken(tokenValue);
        if (accessToken == null) {
            throw new AccessTokenNotFound("Access token not found");
        }
        if (accessToken.isUsed()) {
            throw new IllegalStateException("Token has already been used");
        }
        if (accessToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            accessToken.setStatus("expired");
            instance.accessTokenRepository.save(accessToken);
            throw new IllegalStateException("Token has expired");
        }
        return accessToken;
    }
}