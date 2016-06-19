package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ValueSimpleTest extends TestConfigBase {
    @Test
    public void testIsValue() throws Exception {
        assertTrue(ValueSimple.is(getPreparedConfig("v: 1"), "v"));
    }

    @Test
    public void testIsNotValue() throws Exception {
        assertFalse(ValueSimple.is(getPreparedConfig("v: 1.0"), "v"));
    }

    @Test
    public void testEmptyValue() throws Exception {
        assertFalse(ValueSimple.is(getPreparedConfig(""), "v"));
    }
}