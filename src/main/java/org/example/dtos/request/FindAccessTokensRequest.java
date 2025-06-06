package org.example.dtos.request;

import lombok.Data;

@Data
public class FindAccessTokensRequest {
    private String status;
    private String search;
    private String token;
}