package com.hssa.common.constants;

/**
 * Global application constants.
 */
public final class ApplicationConstants {

    private ApplicationConstants() {
        // Prevent instantiation
    }

    public static final String APP_NAME = "Humanities & Social Sciences Alive Backend";
    public static final String API_VERSION = "/api/v1";
    
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String TIMEZONE = "UTC";
    public static final String CHARACTER_ENCODING = "UTF-8";
    
    public static final String MEDIA_TYPE_JSON = "application/json";
    
    public static final String JWT_PREFIX_PLACEHOLDER = "Bearer ";
    
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_INSTRUCTOR = "ROLE_INSTRUCTOR";
    public static final String ROLE_USER = "ROLE_USER";
    
    public static final String MSG_SUCCESS = "Operation successful";
    public static final String MSG_CREATED = "Resource created successfully";
    public static final String MSG_UPDATED = "Resource updated successfully";
    public static final String MSG_DELETED = "Resource deleted successfully";
    public static final String MSG_NOT_FOUND = "Resource not found";
    public static final String MSG_UNAUTHORIZED = "Unauthorized access";
    public static final String MSG_FORBIDDEN = "Access forbidden";
    public static final String MSG_VALIDATION_FAILED = "Validation failed";
    public static final String MSG_INTERNAL_ERROR = "Internal server error";
}
