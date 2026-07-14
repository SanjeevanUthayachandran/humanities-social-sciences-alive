package com.hssa.workshop.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkshopStatisticsResponse {
    private long totalWorkshops;
    private long upcoming;
    private long ongoing;
    private long completed;
    private long cancelled;
    private long totalRegistrations;
}
