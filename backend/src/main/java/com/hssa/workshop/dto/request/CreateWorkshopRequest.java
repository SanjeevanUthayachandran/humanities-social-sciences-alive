package com.hssa.workshop.dto.request;

import com.hssa.workshop.enums.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateWorkshopRequest {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    @NotNull(message = "Event type is required")
    private EventType eventType;
    private UUID coverMediaId;
    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;
    @NotNull(message = "End time is required")
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
