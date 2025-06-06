package org.example.services;



import org.example.dtos.request.*;
import org.example.dtos.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResidentServices {
    RegisterResidentResponse register(RegisterResidentRequest request);
    LoginResidentResponse login(LoginResidentRequest request);
    GenerateAccessTokenResponse generateAccessToken(GenerateAccessTokenRequest request, String token);
    List<FindAccessTokenResponse> findAccessTokens(FindAccessTokensRequest request, String token);
    void revokeAccessToken(RevokeAccessTokenRequest request, String token);
    UpdateResidentProfileResponse updateProfile(UpdateResidentProfileRequest request, String token);
    void changePassword(ChangePasswordRequest request, String token);
    String uploadProfilePicture(MultipartFile file, String token);
    List<VisitorHistoryResponse> getVisitorHistory(VisitorHistoryRequest request, String token);
    DashboardStatsResponse getDashboardStats(String token);
}