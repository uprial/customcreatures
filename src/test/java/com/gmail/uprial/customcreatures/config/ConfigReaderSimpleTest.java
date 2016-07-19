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

    // ==== getBoolean ====
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

    // ==== getInt ====
    @Test
    public void testEmptyInt() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty value number. Use default value 0");
        getInt(getPreparedConfig(""), getDebugFearingCustomLogger(), "n", "value number", 0, 100, 0);
    }

    @Test
    public void testEmptyIntWithoutDefault() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty value number");
        getInt(getPreparedConfig(""), "n", "value number", 0, 100);
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
        e.expectMessage("A value number should be at least 0");
        getInt(getPreparedConfig("n: -1"), getParanoiacCustomLogger(), "n", "value number", 0, 100, 0);
    }

    @Test
    public void testBigInt() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A value number should be at most 100");
        getInt(getPreparedConfig("n: 1000"), getParanoiacCustomLogger(), "n", "value number", 0, 100, 0);
    }

    @Test
    public void testNormalInt() throws Exception {
        assertEquals(50, getInt(getPreparedConfig("n: 50"), getParanoiacCustomLogger(), "n", "value number", 0, 100, 0));
    }

    @Test
    public void testIntMinMaxConflict() throws Exception {
        e.expect(InternalConfigurationError.class);
        e.expectMessage("Max value of value number is greater than max value");
        getInt(getPreparedConfig(""), getParanoiacCustomLogger(), "n", "value number", 200, 100, 0);
    }

    // ==== getDouble ====

    @Test
    public void testBadMin() throws Exception {
        e.expect(InternalConfigurationError.class);
        e.expectMessage("Min value of double value has too many digits");
        getDouble(getPreparedConfig(""), getDebugFearingCustomLogger(), "d", "double value", 0.00001, 100, 0);
    }

    @Test
    public void testBadMax() throws Exception {
        e.expect(InternalConfigurationError.class);
        e.expectMessage("Max value of double value has too many digits");
        getDouble(getPreparedConfig(""), getDebugFearingCustomLogger(), "d", "double value", 0, 100.00001, 0);
    }

    @Test
    public void testBadDefault() throws Exception {
        e.expect(InternalConfigurationError.class);
        e.expectMessage("Default value of double value has too many digits");
        getDouble(getPreparedConfig(""), getDebugFearingCustomLogger(), "d", "double value", 0, 100, 0.00001);
    }

    @Test
    public void testEmptyDouble() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty double value. Use default value 0");
        getDouble(getPreparedConfig(""), getDebugFearingCustomLogger(), "d", "double value", 0, 100, 0);
    }

    @Test
    public void testEmptyDoubleWithoutDefault() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty double value");
        getDouble(getPreparedConfig(""), "d", "double value", 0, 100);
    }

    @Test
    public void testWrongDouble() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A double value is not a double");
        getDouble(getPreparedConfig("n: 1.0.0"), getParanoiacCustomLogger(), "n", "double value", 0, 100, 0);
    }

    @Test
    public void testSmallDouble() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A double value should be at least 0");
        getDouble(getPreparedConfig("n: -1"), getParanoiacCustomLogger(), "n", "double value", 0, 100, 0);
    }

    @Test
    public void testBigDouble() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A double value should be at most 100");
        getDouble(getPreparedConfig("n: 1000"), getParanoiacCustomLogger(), "n", "double value", 0, 100, 0);
    }

    @Test
    public void testBigLeftPart() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A left part of double value has too many digits");
        getDouble(getPreparedConfig("n: 123456789012.0001"), getParanoiacCustomLogger(), "n", "double value", 0, 100, 0);
    }

    @Test
    public void testBigRightPart() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A right part of double value has too many digits");
        getDouble(getPreparedConfig("n: 12345678901.00001"), getParanoiacCustomLogger(), "n", "double value", 0, 100, 0);
    }

    @Test
    public void testNormalDouble() throws Exception {
        assertEquals(50, getDouble(getPreparedConfig("n: 50"), getParanoiacCustomLogger(), "n", "double value", 0, 100, 0), Double.MIN_VALUE);
    }

    @Test
    public void testNormalIntDouble() throws Exception {
        assertEquals(50, getDouble(getPreparedConfig("n: 50"), getParanoiacCustomLogger(), "n", "double value", 0, 100, 0), Double.MIN_VALUE);
    }

    @Test
    public void testDoubleMinMaxConflict() throws Exception {
        e.expect(InternalConfigurationError.class);
        e.expectMessage("Max value of value number is greater than max value");
        getDouble(getPreparedConfig(""), getParanoiacCustomLogger(), "n", "value number", 200, 100, 0);
    }
}