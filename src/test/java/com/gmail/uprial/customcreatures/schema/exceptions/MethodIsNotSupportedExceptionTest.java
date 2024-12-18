package com.gmail.uprial.customcreatures.schema.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MethodIsNotSupportedExceptionTest {
    @Test
    public void testMessage() {
        assertEquals("Method 'method' isn't supported by this version of Minecraft",
                new MethodIsNotSupportedException("method").getMessage());
    }
}