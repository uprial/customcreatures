package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class AbstractValueSimpleTest extends TestConfigBase {
    @Test
    public void testIntIsValue() throws Exception {
        assertTrue(AbstractValueSimple.is(getPreparedConfig("v: 1"), "v"));
    }

    @Test
    public void testFloatIsValue() throws Exception {
        assertTrue(AbstractValueSimple.is(getPreparedConfig("v: 1.0"), "v"));
    }

    @Test
    public void testIsNotValue() throws Exception {
        assertFalse(AbstractValueSimple.is(getPreparedConfig("v: 1z.0"), "v"));
    }

    @Test
    public void testEmptyValue() throws Exception {
        assertFalse(AbstractValueSimple.is(getPreparedConfig(""), "v"));
    }

}