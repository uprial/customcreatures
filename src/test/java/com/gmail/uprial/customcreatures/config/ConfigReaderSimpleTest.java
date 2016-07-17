package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.config.ConfigReaderSimple.*;
import static org.junit.Assert.*;

public class ConfigReaderSimpleTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyBoolean() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty 'value' flag. Use default value false");
        getBoolean(getPreparedConfig(""), getDebugFearingCustomLogger(), "f", "'value' flag", false);
    }

    @Test
    public void testEmptyBooleanDefaultValue() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty 'value' flag. Use default value true");
        getBoolean(getPreparedConfig(""), getDebugFearingCustomLogger(), "f", "'value' flag", true);
    }

    @Test
    public void testInvalidBoolean() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid 'value' flag. Use default value false");
        getBoolean(getPreparedConfig("f: x"), getParanoiacCustomLogger(), "f", "'value' flag", false);
    }

    @Test
    public void testBooleanTrue() throws Exception {
        assertTrue(getBoolean(getPreparedConfig("f: true"), getParanoiacCustomLogger(), "f", "'value' flag", true));
    }

    @Test
    public void testBooleanTrueDifferentCase() throws Exception {
        assertTrue(getBoolean(getPreparedConfig("f: True"), getParanoiacCustomLogger(), "f", "'value' flag", true));
    }

    @Test
    public void testBooleanFalseDifferentCase() throws Exception {
        assertFalse(getBoolean(getPreparedConfig("f: False"), getParanoiacCustomLogger(), "f", "'value' flag", true));
    }

    @Test
    public void testEmptyInt() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty value number. Use default value 0");
        getInt(getPreparedConfig(""), getDebugFearingCustomLogger(), "n", "value number", 0, 100, 0);
    }

    @Test
    public void testWrongInt() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A value number is not an integer");
        getInt(getPreparedConfig("n: 1.0"), getParanoiacCustomLogger(), "n", "value number", 0, 100, 0);
    }

    @Test
    public void testSmallInt() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A value number should be at least 0. Use default value 0");
        getInt(getPreparedConfig("n: -1"), getParanoiacCustomLogger(), "n", "value number", 0, 100, 0);
    }

    @Test
    public void testBigInt() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A value number should be at most 100. Use default value 0");
        getInt(getPreparedConfig("n: 1000"), getParanoiacCustomLogger(), "n", "value number", 0, 100, 0);
    }

    @Test
    public void testNormalInt() throws Exception {
        assertEquals(50, getInt(getPreparedConfig("n: 50"), getParanoiacCustomLogger(), "n", "value number", 0, 100, 0));
    }

    @Test
    public void testEmptyStrictInt() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty value number");
        getInt(getPreparedConfig(""), "n", "value number");
    }

    @Test
    public void testWrongStrictInt() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A value number is not an integer");
        getInt(getPreparedConfig("n: 1.0"), "n", "value number");
    }

    @Test
    public void testNormalStrictInt() throws Exception {
        assertEquals(50, getInt(getPreparedConfig("n: 50"), "n", "value number"));
    }

    @Test
    public void testEmptyStrictDouble() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty value number");
        getDouble(getPreparedConfig(""), "n", "value number");
    }

    @Test
    public void testWrongStrictDouble() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A value number is not a double");
        getDouble(getPreparedConfig("n: 1z.0"), "n", "value number");
    }

    @Test
    public void testNormalStrictDouble() throws Exception {
        assertEquals(50, Math.round(getDouble(getPreparedConfig("n: 50.0"), "n", "value number")));
    }

    @Test
    public void testIntStrictDouble() throws Exception {
        assertEquals(50, Math.round(getDouble(getPreparedConfig("n: 50"), "n", "value number")));
    }
}