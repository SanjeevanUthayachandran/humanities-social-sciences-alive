package com.hssa.event.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EventDetailResponse {
    private UUID id;
    private String title;
    private String slug;
    private String description;
    private String eventType;
    private String status;
    private UUID coverMediaId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String locationType;
    private String locationDetails;
    private LocalDateTime registrationDeadline;
    private String timezone;
    private String meetingLink;
    private Integer maxParticipants;
    private Boolean isFree;
    private BigDecimal price;
    private String organizerName;
    private LocalDateTime createdAt;
}