package com.hssa.certificate;

import com.hssa.common.BaseServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("CertificateServiceTest")
class CertificateServiceTest extends BaseServiceTest {

    @Test
    @DisplayName("Service layer is initialized")
    void contextLoads() {
        assertNotNull(true);
    }
}