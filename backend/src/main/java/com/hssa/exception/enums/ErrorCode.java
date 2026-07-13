package com.hssa.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    COMMON_001("Internal Server Error"),
    COMMON_002("Resource Not Found"),
    VALIDATION_001("Validation Failed"),
    SECURITY_001("Unauthorized Access"),
    SECURITY_002("Forbidden Access"),
    DATABASE_001("Data Integrity Violation");

    private final String defaultMessage;
}
