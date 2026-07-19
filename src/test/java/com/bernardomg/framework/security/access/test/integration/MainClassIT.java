package com.bernardomg.framework.security.access.test.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.bernardomg.framework.security.access.MainClass;

class MainClassIT {

    @Test
    void integrationSuiteIsConfigured() {
        assertNotNull(MainClass.projectName());
    }
}
