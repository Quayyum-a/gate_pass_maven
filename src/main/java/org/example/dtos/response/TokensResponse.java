package org.example.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class TokensResponse {
    private List<FindAccessTokenResponse> tokens;

    public TokensResponse(List<FindAccessTokenResponse> tokens) {
        this.tokens = tokens;
    }
}