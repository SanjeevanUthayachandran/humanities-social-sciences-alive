package com.hssa.event.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventStatisticsResponse {
    private long totalEvents;
    private long upcoming;
    private long ongoing;
    private long completed;
    private long cancelled;
    private long totalRegistrations;
}