package com.hssa.common;

import com.hssa.auth.entity.User;

import java.util.UUID;

public class TestDataFactory {

    public static User createTestUser() {
        User user = new User();
        user.setId(TestConstants.TEST_USER_ID);
        user.setEmail(TestConstants.TEST_EMAIL);
        user.setFirstName("Test");
        user.setLastName("User");
        return user;
    }

    public static User createAdminUser() {
        User user = new User();
        user.setId(TestConstants.TEST_ADMIN_ID);
        user.setEmail(TestConstants.ADMIN_EMAIL);
        user.setFirstName("Admin");
        user.setLastName("User");
        return user;
    }

    public static User createInstructorUser() {
        User user = new User();
        user.setId(TestConstants.TEST_INSTRUCTOR_ID);
        user.setEmail(TestConstants.INSTRUCTOR_EMAIL);
        user.setFirstName("Instructor");
        user.setLastName("User");
        return user;
    }
}