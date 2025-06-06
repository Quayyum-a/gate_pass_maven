package org.example.services;

import org.example.data.models.AccessToken;
import org.example.data.models.Resident;
import org.example.data.models.Visitor;
import org.example.data.repositories.AccessTokens;
import org.example.data.repositories.Residents;
import org.example.data.repositories.Visitors;
import org.example.dtos.request.*;
import org.example.dtos.response.*;
import org.example.exceptions.GatePassException;
import org.example.exceptions.ResidentAlreadyExsitsException;
import org.example.exceptions.ResidentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.utils.Mapper.*;

@Service
public class ResidentServicesImpl implements ResidentServices {
    private final Residents residentRepository;
    private final AccessTokens accessTokenRepository;
    private final Visitors visitorRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ResidentServicesImpl(
            Residents residentRepository,
            AccessTokens accessTokenRepository,
            Visitors visitorRepository) {
        this.residentRepository = residentRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.visitorRepository = visitorRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
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
        resident.setPassword(passwordEncoder.encode(request.getPassword()));
        Resident savedResident = residentRepository.save(resident);
        return mapToRegisterResidentResponse(savedResident);
    }

    @Override
    public LoginResidentResponse login(LoginResidentRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Email and password are required");
        }
        Resident resident = residentRepository.findByEmail(request.getEmail());
        if (resident == null || !passwordEncoder.matches(request.getPassword(), resident.getPassword())) {
            throw new GatePassException("Invalid email or password");
        }
        return mapToLoginResidentResponse(resident);
    }

    @Override
    @Transactional
    public GenerateAccessTokenResponse generateAccessToken(GenerateAccessTokenRequest request, String token) {
        Resident resident = validateTokenAndGetResident(token);
        Visitor visitor = visitorInformation(request);
        visitor.setCheckIn(LocalDateTime.now());
        Visitor savedVisitor = visitorRepository.save(visitor);
        AccessToken accessToken = accessTokenInformation(resident, savedVisitor);
        accessToken.setVisitorName(savedVisitor.getFullName());
        accessToken.setVisitorPhoneNumber(savedVisitor.getPhoneNumber());
        accessToken.setWhomToSee(resident.getFullName());
        accessToken.setCreationDate(LocalDateTime.now());
        accessToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        accessToken.setUsed(false);
        accessToken.setStatus("active");
        AccessToken savedToken = accessTokenRepository.save(accessToken);
        return mapToAccessTokenResponse(savedToken);
    }

    @Override
    public List<FindAccessTokenResponse> findAccessTokens(FindAccessTokensRequest request, String token) {
        Resident resident = validateTokenAndGetResident(token);
        List<AccessToken> tokens;
        if (request.getStatus() != null && !request.getStatus().equals("all")) {
            tokens = accessTokenRepository.findByResidentIdAndStatus(resident.getId(), request.getStatus());
        } else if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            tokens = accessTokenRepository.findBySearchTermAndResidentId(request.getSearch(), resident.getId());
        } else {
            tokens = accessTokenRepository.findByResidentId(resident.getId());
        }
        return tokens.stream().map(this::mapToFindAccessTokenResponse).collect(Collectors.toList());
    }

    @Override
    public void revokeAccessToken(RevokeAccessTokenRequest request, String token) {
        Resident resident = validateTokenAndGetResident(token);
        AccessToken accessToken = accessTokenRepository.findByToken(request.getToken());
        if (accessToken == null || !accessToken.getResident().getId().equals(resident.getId())) {
            throw new GatePassException("Invalid or unauthorized token");
        }
        if (accessToken.isUsed() || accessToken.getStatus().equals("expired")) {
            throw new GatePassException("Token is already used or expired");
        }
        accessToken.setStatus("revoked");
        accessTokenRepository.save(accessToken);
    }

    @Override
    public UpdateResidentProfileResponse updateProfile(UpdateResidentProfileRequest request, String token) {
        Resident resident = validateTokenAndGetResident(token);
        resident.setFullName(request.getFullName());
        resident.setPhoneNumber(request.getPhoneNumber());
        resident.setAddress(request.getAddress());
        Resident savedResident = residentRepository.save(resident);
        UpdateResidentProfileResponse response = new UpdateResidentProfileResponse();
        response.setFullName(savedResident.getFullName());
        response.setEmail(savedResident.getEmail());
        response.setPhoneNumber(savedResident.getPhoneNumber());
        response.setAddress(savedResident.getAddress());
        return response;
    }

    @Override
    public void changePassword(ChangePasswordRequest request, String token) {
        Resident resident = validateTokenAndGetResident(token);
        if (!passwordEncoder.matches(request.getCurrentPassword(), resident.getPassword())) {
            throw new GatePassException("Current password is incorrect");
        }
        resident.setPassword(passwordEncoder.encode(request.getNewPassword()));
        residentRepository.save(resident);
    }

    @Override
    public String uploadProfilePicture(MultipartFile file, String token) {
        Resident resident = validateTokenAndGetResident(token);
        // Implement file storage logic (e.g., save to filesystem or cloud storage)
        // Return the URL of the stored file
        return "http://localhost:8080/uploads/" + file.getOriginalFilename();
    }

    @Override
    public List<VisitorHistoryResponse> getVisitorHistory(VisitorHistoryRequest request, String token) {
        Resident resident = validateTokenAndGetResident(token);
        LocalDateTime fromDate = request.getFromDate() != null ? LocalDateTime.parse(request.getFromDate(), DateTimeFormatter.ISO_LOCAL_DATE) : LocalDateTime.now().minusDays(30);
        LocalDateTime toDate = request.getToDate() != null ? LocalDateTime.parse(request.getToDate(), DateTimeFormatter.ISO_LOCAL_DATE) : LocalDateTime.now();
        List<Visitor> visitors = visitorRepository.findByResidentIdAndDateRange(resident.getId(), fromDate, toDate);
        return visitors.stream().map(this::mapToVisitorHistoryResponse).collect(Collectors.toList());
    }

    @Override
    public DashboardStatsResponse getDashboardStats(String token) {
        Resident resident = validateTokenAndGetResident(token);
        DashboardStatsResponse response = new DashboardStatsResponse();
        response.setTokensGenerated(accessTokenRepository.findByResidentId(resident.getId()).size());
        response.setActiveVisitors(visitorRepository.findByResidentIdAndDateRange(resident.getId(), LocalDateTime.now().minusDays(1), LocalDateTime.now())
                .stream().filter(v -> v.getCheckOut() == null).count());
        return response;
    }

    private Resident validateTokenAndGetResident(String token) {
        // Simplified token validation (in production, use JWT verification)
        String email = extractEmailFromToken(token);
        Resident resident = residentRepository.findByEmail(email);
        if (resident == null) {
            throw new ResidentNotFoundException("Resident not found");
        }
        return resident;
    }

    private String extractEmailFromToken(String token) {
        // Simplified token parsing (in production, decode JWT)
        return "user@example.com";
    }

    private FindAccessTokenResponse mapToFindAccessTokenResponse(AccessToken accessToken) {
        FindAccessTokenResponse response = new FindAccessTokenResponse();
        response.setVisitorName(accessToken.getVisitorName());
        response.setVisitorPhoneNumber(accessToken.getVisitorPhoneNumber());
        response.setWhomToSee(accessToken.getWhomToSee());
        response.setResidentFullName(accessToken.getResident().getFullName());
        response.setResidentAddress(accessToken.getResident().getAddress());
        response.setResidentEmail(accessToken.getResident().getEmail());
        response.setIsValid(!accessToken.isUsed() && accessToken.getExpiryDate().isAfter(LocalDateTime.now()));
        response.setExpiryDate(accessToken.getExpiryDate());
        return response;
    }

    private VisitorHistoryResponse mapToVisitorHistoryResponse(Visitor visitor) {
        VisitorHistoryResponse response = new VisitorHistoryResponse();
        response.setName(visitor.getFullName());
        response.setPhone(visitor.getPhoneNumber());
        response.setWhomToSee(visitor.getWhomToSee().getFullName());
        response.setVisitDate(visitor.getCheckIn());
        response.setPurpose(visitor.getPurpose());
        return response;
    }
}