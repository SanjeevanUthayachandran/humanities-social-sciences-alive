package com.hssa.exception.constants;

public class ExceptionConstants {
    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred.";
    public static final String RESOURCE_NOT_FOUND = "The requested resource could not be found.";
    public static final String METHOD_NOT_ALLOWED = "The HTTP method is not supported for this endpoint.";
    public static final String VALIDATION_FAILED = "Request validation failed.";
    public static final String DATA_INTEGRITY_VIOLATION = "Database constraint violation.";
    public static final String UNAUTHORIZED = "Authentication is required to access this resource.";
    public static final String FORBIDDEN = "You do not have permission to access this resource.";
    public static final String BAD_REQUEST = "Malformed or invalid request.";
    public static final String PAYLOAD_TOO_LARGE = "The uploaded file exceeds the maximum allowed size.";

    private ExceptionConstants() {}
}
