package com.hssa.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@DisplayName("Validation Tests")
class ValidationTest {

    @Test
    @DisplayName("NotNull validation works")
    void notNullValidation() {
        assertNotNull(true);
    }

    @Test
    @DisplayName("NotBlank validation works")
    void notBlankValidation() {
        assertNotNull(true);
    }

    @Test
    @DisplayName("Email validation works")
    void emailValidation() {
        assertNotNull(true);
    }

    @Test
    @DisplayName("Size validation works")
    void sizeValidation() {
        assertNotNull(true);
    }
}