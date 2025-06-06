package org.example.services;


import org.example.dtos.request.*;
import org.example.dtos.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SecurityServices {
    RegisterSecurityResponse register(RegisterSecurityRequest request);
    LoginSecurityResponse login(LoginSecurityRequest request);
    FindAccessTokenResponse findAccessToken(FindAccessTokensRequest request, String token);
    List<VisitorLogResponse> getVisitorLogs(VisitorLogsRequest request, String token);
    UpdateSecurityProfileResponse updateProfile(UpdateSecurityProfileRequest request, String token);
    void changePassword(ChangePasswordRequest request, String token);
    String uploadProfilePicture(MultipartFile file, String token);
    DashboardStatsResponse getDashboardStats(String token);
}