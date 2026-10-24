package com.hssa.payment.dto.response;

import java.util.UUID;

public record CoursePaymentSettingsResponse(
        UUID courseId,
        String gatewayCode,
        boolean onlinePaymentEnabled,
        boolean bankTransferEnabled,
        boolean installmentsEnabled) {
}
