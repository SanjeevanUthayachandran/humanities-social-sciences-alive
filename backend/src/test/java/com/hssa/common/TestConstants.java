package com.hssa.common;

import java.util.UUID;

public class TestConstants {
    public static final UUID TEST_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    public static final UUID TEST_ADMIN_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    public static final UUID TEST_INSTRUCTOR_ID = UUID.fromString("00000000-0000-0000-0000-000000000003");
    public static final UUID TEST_COURSE_ID = UUID.fromString("00000000-0000-0000-0000-000000000010");
    public static final UUID TEST_ENROLLMENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000020");
    public static final UUID TEST_PAYMENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000030");

    public static final String TEST_EMAIL = "test@hssa.lk";
    public static final String ADMIN_EMAIL = "admin@hssa.lk";
    public static final String INSTRUCTOR_EMAIL = "instructor@hssa.lk";
    public static final String TEST_PASSWORD = "Test@12345";
    public static final String TEST_JWT_TOKEN = "test-jwt-token";
}