package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertEquals(1, ValueSimple.getIntFromConfig(getPreparedConfig("v: 1.6"), "v").getValue().intValue());
    }

    @Test
    public void testDoubleValue() throws Exception {
        assertEquals(1, Math.round(ValueSimple.getDoubleFromConfig(getPreparedConfig("v: 1.0"), "v").getValue()));
    }
}