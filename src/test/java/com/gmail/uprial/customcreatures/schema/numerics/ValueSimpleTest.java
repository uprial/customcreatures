package com.gmail.uprial.customcreatures.schema.numerics;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValueSimpleTest extends TestConfigBase {
    @Test
    public void testIntIsValue() throws Exception {
        assertTrue(ValueSimple.is(getPreparedConfig("v: 1"), "v"));
    }

    @Test
    public void testFloatIsValue() throws Exception {
        assertTrue(ValueSimple.is(getPreparedConfig("v: 1.0"), "v"));
    }

    @Test
    public void testIsNotValue() throws Exception {
        assertFalse(ValueSimple.is(getPreparedConfig("v: 1z.0"), "v"));
    }

    @Test
    public void testEmptyValue() throws Exception {
        assertFalse(ValueSimple.is(getPreparedConfig(""), "v"));
    }

    @Test
    public void testIntValue() throws Exception {
        assertEquals(1, ValueSimple.getIntFromConfig(getPreparedConfig("v: 1"), getCustomLogger(), "v", "v", 0, 100).getValue().intValue());
    }

    @Test
    public void testDoubleValue() throws Exception {
        assertEquals(1, Math.round(ValueSimple.getDoubleFromConfig(getPreparedConfig("v: 1.0"), getCustomLogger(), "v", "v", 0, 100).getValue()));
    }
}