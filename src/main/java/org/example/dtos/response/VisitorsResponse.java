package org.example.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class VisitorsResponse {
    private List<VisitorHistoryResponse> visitors;

    public VisitorsResponse(List<VisitorHistoryResponse> visitors) {
        this.visitors = visitors;
    }
}