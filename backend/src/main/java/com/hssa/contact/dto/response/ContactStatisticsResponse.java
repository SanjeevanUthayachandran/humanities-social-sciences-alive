package com.hssa.contact.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactStatisticsResponse {
    private long totalMessages;
    private long newMessages;
    private long inProgressMessages;
    private long resolvedMessages;
    private long closedMessages;
}