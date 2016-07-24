package com.gmail.uprial.customcreatures.schema.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OperationIsNotSupportedExceptionTest {
    @Test
    public void testMessage() throws Exception {
        assertEquals("Operation isn't supported",
                new OperationIsNotSupportedException().getMessage());
    }

}