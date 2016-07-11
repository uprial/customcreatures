package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import com.gmail.uprial.customcreatures.helpers.TestEnum;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.Set;

import static com.gmail.uprial.customcreatures.config.ConfigReader.*;
import static org.junit.Assert.*;

public class ConfigReaderTest extends TestConfigBase {
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
        getBoolean(getPreparedConfig("f: x"), getCustomLogger(), "f", "'value' flag", false);
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
    public void testEmptyStringList() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty list. Use default value NULL");
        getStringList(getPreparedConfig("sl: "), getDebugFearingCustomLogger(), "sl", "list");
    }

    @Test
    public void testEmptyStringListValue() throws Exception {
        assertEquals(null, getStringList(getPreparedConfig("sl: "), getParanoiacCustomLogger(), "sl", "list"));
    }

    @Test
    public void testStringList() throws Exception {
        List<String> sl = getStringList(getPreparedConfig("sl: ", " - x"), getDebugFearingCustomLogger(), "sl", "list");
        //noinspection ConstantConditions
        assertEquals(1, sl.size());
        assertEquals("x", sl.get(0));
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
        getInt(getPreparedConfig("n: 1.0"), getCustomLogger(), "n", "value number", 0, 100, 0);
    }

    @Test
    public void testSmallInt() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A value number should be at least 0. Use default value 0");
        getInt(getPreparedConfig("n: -1"), getCustomLogger(), "n", "value number", 0, 100, 0);
    }

    @Test
    public void testBigInt() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A value number should be at most 100. Use default value 0");
        getInt(getPreparedConfig("n: 1000"), getCustomLogger(), "n", "value number", 0, 100, 0);
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

    @Test
    public void testEmptyString() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null or empty string");
        getString(getPreparedConfig("s:"), "s", "string");
    }

    @Test
    public void testNormalString() throws Exception {
        assertEquals("val", getString(getPreparedConfig("s: val"), "s", "string"));
    }

    @Test
    public void testWrongEnumString() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid com.gmail.uprial.customcreatures.helpers.TestEnum 'T' in enum");
        getEnumFromString(TestEnum.class, "T", "enum", "");
    }

    @Test
    public void testNormalEnumString() throws Exception {
        assertEquals(TestEnum.A, getEnumFromString(TestEnum.class, "A", "enum", ""));
    }

    @Test
    public void testNormalEnum() throws Exception {
        assertEquals(TestEnum.A, getEnum(TestEnum.class, getPreparedConfig("e: A"), "e", "enum"));
    }

    @Test
    public void testNormalEnumCase() throws Exception {
        assertEquals(TestEnum.A, getEnumFromString(TestEnum.class, "a", "enum", ""));
    }

    @Test
    public void testWrongEnum() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid com.gmail.uprial.customcreatures.helpers.TestEnum 'T' in enum");
        getEnum(TestEnum.class, getPreparedConfig("e: T"), "e", "enum");
    }

    @Test
    public void testEmptySet() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty set. Use default value NULL");
        getSet(TestEnum.class, getPreparedConfig(""), getDebugFearingCustomLogger(), "s", "set");
    }

    @Test
    public void testEmptySetValue() throws Exception {
        assertEquals(null, getSet(TestEnum.class, getPreparedConfig(""), getParanoiacCustomLogger(), "s", "set"));
    }

    @Test
    public void testWrongSetType() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid com.gmail.uprial.customcreatures.helpers.TestEnum 'Z' in set at pos 0");
        getSet(TestEnum.class, getPreparedConfig("x:", "  entities:", "   - Z"), getCustomLogger(),
                "x.entities", "set");
    }

    @Test
    public void testNotUniqueSet() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("com.gmail.uprial.customcreatures.helpers.TestEnum 'A' in set is not unique");
        getSet(TestEnum.class, getPreparedConfig("x:", "  entities:", "   - A", "   - A"),
                getCustomLogger(), "x.entities", "set");
    }

    @Test
    public void testNormalSet() throws Exception {
        //noinspection ConstantConditions
        assertEquals("[A]", getSet(TestEnum.class, getPreparedConfig("entities:", " - A"),
                getParanoiacCustomLogger(), "entities", "path").toString());
    }

    @Test
    public void testContentOfSet() throws Exception {
        Set<TestEnum> entities = getSet(TestEnum.class, getPreparedConfig("entities:", " - A", " - B"),
                getParanoiacCustomLogger(), "entities", "path");
        //noinspection ConstantConditions
        assertEquals(2, entities.size());
        assertTrue(entities.contains(TestEnum.A));
        assertTrue(entities.contains(TestEnum.B));
    }

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

    @Test
    public void testNoItems() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty items list. Use default value NULL");
        getItemsList(getPreparedConfig(
                "?:"),
                getDebugFearingCustomLogger(), "ee", "items list");
    }

    @Test
    public void testEmptyItemsList() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty items list. Use default value NULL");
        getItemsList(getPreparedConfig(
                "x:"),
                getDebugFearingCustomLogger(), "x", "items list");
    }

    @Test
    public void testEmptyItemsValue() throws Exception {
        Set<String> items = getItemsList(getPreparedConfig(
                "x:"),
                getParanoiacCustomLogger(), "x", "items list");
        assertEquals(null, items);
    }

    @Test
    public void testNoDefinitionOfKeyInItems() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null definition of key 'y' in items list");
        getItemsList(getPreparedConfig(
                "x:",
                " - y"),
                getParanoiacCustomLogger(), "x", "items list");
    }

    @Test
    public void testNoDefinitionOfKeyWithParentsInItems() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null definition of keys 'x.z' and 'z' in items list");
        getItemsList(getPreparedConfig(
                "x:",
                " y:",
                "  - z"),
                getParanoiacCustomLogger(), "x.y", "items list");
    }

    @Test
    public void testNotUniqueKeyInItems() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Key 'y' in items list is not unique");
        getItemsList(getPreparedConfig(
                "x:",
                " - y",
                " - y",
                "y:",
                " - z"),
                getParanoiacCustomLogger(), "x", "items list");
    }

    @Test
    public void testAbsoluteItemPath() throws Exception {
        Set<String> items = getItemsList(getPreparedConfig(
                "x:",
                " y:",
                "  - x.z",
                " z:",
                "  - zz"),
                getParanoiacCustomLogger(), "x.y", "items list");
        //noinspection ConstantConditions
        assertEquals("[x.z]", items.toString());
    }

    @Test
    public void testRelativeEffectPath() throws Exception {
        Set<String> items = getItemsList(getPreparedConfig(
                "x:",
                " y:",
                "  - z",
                " z:",
                "  - zz"),
                getParanoiacCustomLogger(), "x.y", "effects list");
        //noinspection ConstantConditions
        assertEquals("[x.z]", items.toString());
    }


}