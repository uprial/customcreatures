package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static com.gmail.uprial.customcreatures.config.ConfigReaderSimple.*;
import static org.junit.Assert.*;

public class ConfigReaderSimpleTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    // ==== getKey ====
    @Test
    public void testNullKey() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null key in keys at pos 0");
        getKey(null, "keys", 0);
    }

    @Test
    public void testEmptyKey() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty key in keys at pos 0");
        getKey("", "keys", 0);
    }

    @Test
    public void testNotStringKey() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Key '1' in keys at pos 0 is not a string");

        getKey(1, "keys", 0);
    }

    @Test
    public void testNormalKey() throws Exception {
        assertEquals("k", getKey("k", "keys", 0));
    }

    // ==== getString ====
    @Test
    public void testNullString() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null string");
        getString(getPreparedConfig("s:"), "s", "string");
    }

    @Test
    public void testEmptyString() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty string");
        getString(getPreparedConfig("s: ''"), "s", "string");
    }

    @Test
    public void testNormalString() throws Exception {
        assertEquals("val", getString(getPreparedConfig("s: val"), "s", "string"));
    }

    // ==== getStringList ====
    @Test
    public void testEmptyStringList() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty list. Use default value NULL");
        getStringList(getPreparedConfig("sl: "), getDebugFearingCustomLogger(), "sl", "list");
    }

    @Test
    public void testEmptyStringListValue() throws Exception {
        assertNull(getStringList(getPreparedConfig("sl: "), getCustomLogger(), "sl", "list"));
    }

    @Test
    public void testStringList() throws Exception {
        List<String> sl = getStringList(getPreparedConfig("sl: ", " - x"), getDebugFearingCustomLogger(), "sl", "list");
        assertNotNull(sl);
        assertEquals(1, sl.size());
        assertEquals("x", sl.get(0));
    }

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
    public void testEmptyBooleanNoDefaultValue() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty 'value' flag");
        getBoolean(getPreparedConfig(""), getDebugFearingCustomLogger(), "f", "'value' flag");
    }

    @Test
    public void testInvalidBoolean() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid 'value' flag");
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
}