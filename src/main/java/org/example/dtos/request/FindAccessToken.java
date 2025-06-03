package org.example.dtos.request;

import lombok.Data;

@Data
public class FindAccessToken {
    private String email;
    private String accessCode;
}

