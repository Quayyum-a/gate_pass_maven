package org.example.dtos.request;

import lombok.Data;

@Data
public class VisitorHistoryRequest {
    private String fromDate;
    private String toDate;
}