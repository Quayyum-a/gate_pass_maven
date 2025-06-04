package org.example.verification;

import org.example.data.models.AccessToken;
import org.example.data.models.Security;
import org.example.data.repositories.AccessTokens;
import org.example.data.repositories.Securities;
import org.example.exceptions.AccessTokenNotFound;
import org.example.exceptions.GatePassException;
import org.example.exceptions.SecurityAlreadyExsistException;
import org.springframework.beans.factory.annotation.Autowired;

public class Verification {
    @Autowired
    private static Securities securities;

    public static void verifyEmail(String email) {
        if (securities.findByEmail(email) != null) {
            throw new SecurityAlreadyExsistException("Email already exists");
        }
    }

    @Autowired
    private static Securities securityRepository;

    public static void verifyEmailAndPassword(String email, String password) {
        Security security = securityRepository.findByEmail(email);
        if (security == null || !security.getPassword().equals(password)) {
            throw new GatePassException("Invalid email or password");
        }
    }

    @Autowired
    public static AccessTokens accessTokenRepository;
    public static void verifyToken(AccessToken token) {
        AccessToken accessToken = accessTokenRepository.findByToken(token.getToken());
        if (accessToken == null) {
            throw new AccessTokenNotFound("Access token not found");
        }
    }
}