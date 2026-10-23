package com.hssa.event.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EventSummaryResponse {
    private UUID id;
    private String title;
    private String slug;
    private String description;
    private String eventType;
    private String status;
    private String locationType;
    private String locationDetails;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isFree;
    private BigDecimal price;
    private Integer maxParticipants;
    private String organizerName;
}
