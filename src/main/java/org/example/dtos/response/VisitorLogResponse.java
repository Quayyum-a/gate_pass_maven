package org.example.dtos.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitorLogResponse {
    private String visitorName;
    private String visitorPhone;
    private String resident;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
}