package org.example.services;

import org.example.data.models.AccessToken;
import org.example.data.models.Security;
import org.example.data.models.Visitor;
import org.example.data.repositories.AccessTokens;
import org.example.data.repositories.Securities;
import org.example.data.repositories.Visitors;
import org.example.dtos.request.*;
import org.example.dtos.response.*;
import org.example.exceptions.GatePassException;
import org.example.verification.VerificationNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.utils.Mapper.*;

@Service
public class SecurityServicesImpl implements SecurityServices {
    private final Securities securityRepository;
    private final AccessTokens accessTokenRepository;
    private final Visitors visitorRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SecurityServicesImpl(Securities securityRepository, AccessTokens accessTokenRepository, Visitors visitorRepository) {
        this.securityRepository = securityRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.visitorRepository = visitorRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public RegisterSecurityResponse register(RegisterSecurityRequest request) {
        VerificationNew.verifyEmail(request.getEmail());
        Security security = mapToRegisterSecurityRequest(request);
        security.setPassword(passwordEncoder.encode(request.getPassword()));
        Security savedSecurity = securityRepository.save(security);
        return mapToRegisterSecurityResponse(savedSecurity);
    }

    @Override
    public LoginSecurityResponse login(LoginSecurityRequest request) {
        Security security = securityRepository.findByEmail(request.getEmail());
        if (security == null || !passwordEncoder.matches(request.getPassword(), security.getPassword())) {
            throw new GatePassException("Invalid email or password");
        }
        LoginSecurityResponse response = new LoginSecurityResponse();
        response.setMessage("Login successful");
        response.setFullName(security.getFullName());
        response.setEmail(security.getEmail());
        response.setPhoneNumber(security.getPhoneNumber());
        return response;
    }

    @Override
    public FindAccessTokenResponse findAccessToken(FindAccessTokensRequest request, String token) {
        validateTokenAndGetSecurity(token);
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

    @Override
    public List<VisitorLogResponse> getVisitorLogs(VisitorLogsRequest request, String token) {
        validateTokenAndGetSecurity(token);
        List<Visitor> visitors = request.getSearch() != null && !request.getSearch().isEmpty()
                ? visitorRepository.findBySearchTerm(request.getSearch())
                : visitorRepository.findAll();
        return visitors.stream().map(this::mapToVisitorLogResponse).collect(Collectors.toList());
    }

    @Override
    public UpdateSecurityProfileResponse updateProfile(UpdateSecurityProfileRequest request, String token) {
        Security security = validateTokenAndGetSecurity(token);
        security.setFullName(request.getFullName());
        security.setPhoneNumber(request.getPhoneNumber());
        Security savedSecurity = securityRepository.save(security);
        UpdateSecurityProfileResponse response = new UpdateSecurityProfileResponse();
        response.setFullName(savedSecurity.getFullName());
        response.setEmail(savedSecurity.getEmail());
        response.setPhoneNumber(savedSecurity.getPhoneNumber());
        return response;
    }

    @Override
    public void changePassword(ChangePasswordRequest request, String token) {
        Security security = validateTokenAndGetSecurity(token);
        if (!passwordEncoder.matches(request.getCurrentPassword(), security.getPassword())) {
            throw new GatePassException("Current password is incorrect");
        }
        security.setPassword(passwordEncoder.encode(request.getNewPassword()));
        securityRepository.save(security);
    }

    @Override
    public String uploadProfilePicture(MultipartFile file, String token) {
        validateTokenAndGetSecurity(token);
        // Implement file storage logic (e.g., save to filesystem or cloud storage)
        return "http://localhost:8080/uploads/" + file.getOriginalFilename();
    }

    @Override
    public DashboardStatsResponse getDashboardStats(String token) {
        validateTokenAndGetSecurity(token);
        DashboardStatsResponse response = new DashboardStatsResponse();
        response.setTokensVerified(accessTokenRepository.findAll().stream().filter(AccessToken::isUsed).count());
        return response;
    }

    private Security validateTokenAndGetSecurity(String token) {
        // Simplified token validation (in production, use JWT verification)
        String email = extractEmailFromToken(token);
        Security security = securityRepository.findByEmail(email);
        if (security == null) {
            throw new GatePassException("Security personnel not found");
        }
        return security;
    }

    private String extractEmailFromToken(String token) {
        // Simplified token parsing (in production, decode JWT)
        return "security@example.com";
    }

    private VisitorLogResponse mapToVisitorLogResponse(Visitor visitor) {
        VisitorLogResponse response = new VisitorLogResponse();
        response.setVisitorName(visitor.getFullName());
        response.setVisitorPhone(visitor.getPhoneNumber());
        response.setResident(visitor.getWhomToSee().getFullName());
        response.setCheckIn(visitor.getCheckIn());
        response.setCheckOut(visitor.getCheckOut());
        return response;
    }
}
