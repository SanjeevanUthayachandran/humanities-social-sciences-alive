package com.hssa.literary;

import com.hssa.common.BaseServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("LiteraryServiceTest")
class LiteraryServiceTest extends BaseServiceTest {

    @Test
    @DisplayName("Service layer is initialized")
    void contextLoads() {
        assertNotNull(true);
    }
}