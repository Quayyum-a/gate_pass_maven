package org.example.dtos.request;

import lombok.Data;

@Data
public class RevokeAccessTokenRequest {
    private String token;
}