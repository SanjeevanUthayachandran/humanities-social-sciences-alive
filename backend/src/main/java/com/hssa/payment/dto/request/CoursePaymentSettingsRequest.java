package com.hssa.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CoursePaymentSettingsRequest {
    @NotBlank
    private String gatewayCode;
    private boolean onlinePaymentEnabled;
    private boolean bankTransferEnabled;
    private boolean installmentsEnabled;
}
