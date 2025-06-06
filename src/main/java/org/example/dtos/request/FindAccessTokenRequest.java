package org.example.dtos.request;

import lombok.Data;

/**
 * Request DTO for finding and validating an access token
 */
@Data
public class FindAccessTokenRequest {
    private String token;
}
