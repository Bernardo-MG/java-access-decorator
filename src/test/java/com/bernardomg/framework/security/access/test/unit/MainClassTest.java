package com.bernardomg.framework.security.access.test.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.bernardomg.framework.security.access.MainClass;

class MainClassTest {

    @Test
    void returnsProjectName() {
        assertEquals("Access Decorator", MainClass.projectName());
    }
}
