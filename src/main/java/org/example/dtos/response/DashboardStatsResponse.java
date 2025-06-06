package org.example.dtos.response;

import lombok.Data;

@Data
public class DashboardStatsResponse {
    private long tokensGenerated;
    private long activeVisitors;
    private long tokensVerified; // For security dashboard
}