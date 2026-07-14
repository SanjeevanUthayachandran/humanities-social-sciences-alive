package com.hssa.workshop.dto.request;

import com.hssa.workshop.enums.EventType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UpdateWorkshopRequest {
    private String title;
    private String description;
    private EventType eventType;
    private UUID coverMediaId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String locationType;
    private String locationDetails;
    private LocalDateTime registrationDeadline;
    private String timezone;
    private String meetingLink;
    private String meetingPassword;
    private Integer maxParticipants;
    private Boolean isFree;
    private BigDecimal price;
}
