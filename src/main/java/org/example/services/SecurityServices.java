package org.example.services;

import org.example.dtos.request.FindAccessToken;
import org.example.dtos.request.LoginSecurityRequest;
import org.example.dtos.request.RegisterSecurityRequest;
import org.example.dtos.response.LoginSecurityResponse;
import org.example.dtos.response.RegisterSecurityResponse;

public interface SecurityServices {
    RegisterSecurityResponse register(RegisterSecurityRequest request);

    LoginSecurityResponse login(LoginSecurityRequest request);

    Object findAccessToken(FindAccessToken request);
}
