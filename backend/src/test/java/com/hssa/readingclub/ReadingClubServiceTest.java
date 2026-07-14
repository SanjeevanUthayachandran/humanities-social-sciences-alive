package com.hssa.readingclub;

import com.hssa.common.BaseServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("ReadingClubServiceTest")
class ReadingClubServiceTest extends BaseServiceTest {

    @Test
    @DisplayName("Service layer is initialized")
    void contextLoads() {
        assertNotNull(true);
    }
}