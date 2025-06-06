package org.example.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class LogsResponse {
    private List<VisitorLogResponse> logs;

    public LogsResponse(List<VisitorLogResponse> logs) {
        this.logs = logs;
    }
}