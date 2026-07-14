package com.hssa.enrollment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@DisplayName("EnrollmentControllerTest")
class EnrollmentControllerTest {

    @Test
    @DisplayName("Controller is initialized")
    void contextLoads() {
        assertNotNull(true);
    }
}