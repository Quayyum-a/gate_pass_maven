package org.example.dtos.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitorHistoryResponse {
    private String name;
    private String phone;
    private String whomToSee;
    private LocalDateTime visitDate;
    private String purpose;
}