package com.gmail.uprial.customcreatures.schema.numerics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValueConstTest {
    @Test
    public void testIntValue() {
        assertEquals(1, new ValueConst<>(1).getValue().intValue());
    }

    @Test
    public void testDoubleValue() {
        assertEquals(1, Math.round(new ValueConst<>(1.0).getValue()));
    }
}