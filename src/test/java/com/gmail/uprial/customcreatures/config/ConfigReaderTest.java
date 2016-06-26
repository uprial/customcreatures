package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
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
        e.expectMessage("Empty value flag 'f'. Use default value false");
        getBoolean(getPreparedConfig(""), getDebugFearingCustomLogger(), "f", "value flag", "f", false);
    }

    @Test
    public void testEmptyBooleanDefaultValue() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty value flag 'f'. Use default value true");
        getBoolean(getPreparedConfig(""), getDebugFearingCustomLogger(), "f", "value flag", "f", true);
    }

    @Test
    public void testInvalidBoolean() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid value flag 'f'. Use default value false");
        getBoolean(getPreparedConfig("f: x"), getCustomLogger(), "f", "value flag", "f", false);
    }

    @Test
    public void testBooleanName() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid value flag 'F'. Use default value false");
        getBoolean(getPreparedConfig("f: x"), getCustomLogger(), "f", "value flag", "F", false);
    }

    @Test
    public void testBooleanTrue() throws Exception {
        assertTrue(getBoolean(getPreparedConfig("f: true"), getParanoiacCustomLogger(), "f", "value flag", "f", true));
    }

    @Test
    public void testBooleanTrueDifferentCase() throws Exception {
        assertTrue(getBoolean(getPreparedConfig("f: True"), getParanoiacCustomLogger(), "f", "value flag", "f", true));
    }

    @Test
    public void testBooleanFalseDifferentCase() throws Exception {
        assertFalse(getBoolean(getPreparedConfig("f: False"), getParanoiacCustomLogger(), "f", "value flag", "f", true));
    }

    @Test
    public void testInvalidStringList() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty list 'sl'. Use default value NULL");
        getStringList(getPreparedConfig("sl: "), getDebugFearingCustomLogger(), "sl", "list", "sl");
    }

    @Test
    public void testStringList() throws Exception {
        List<String> sl = getStringList(getPreparedConfig("sl: ", " - x"), getDebugFearingCustomLogger(), "sl", "list", "sl");
        //noinspection ConstantConditions
        assertEquals(1, sl.size());
        assertEquals("x", sl.get(0));
    }

    @Test
    public void testEmptyInt() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty value number 'n'. Use default value 0");
        getInt(getPreparedConfig(""), getDebugFearingCustomLogger(), "n", "value number", "n", 0, 100, 0);
    }

    @Test
    public void testWrongInt() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A value number 'n' is not an integer");
        getInt(getPreparedConfig("n: 1.0"), getCustomLogger(), "n", "value number", "n", 0, 100, 0);
    }

    @Test
    public void testSmallInt() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A value number 'n' should be at least 0. Use default value 0");
        getInt(getPreparedConfig("n: -1"), getCustomLogger(), "n", "value number", "n", 0, 100, 0);
    }

    @Test
    public void testBigInt() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A value number 'n' should be at most 100. Use default value 0");
        getInt(getPreparedConfig("n: 1000"), getCustomLogger(), "n", "value number", "n", 0, 100, 0);
    }

    @Test
    public void testNormalInt() throws Exception {
        assertEquals(50, getInt(getPreparedConfig("n: 50"), getParanoiacCustomLogger(), "n", "value number", "n", 0, 100, 0));
    }

    @Test
    public void testEmptyStrictInt() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty value number 'n'");
        getInt(getPreparedConfig(""), "n", "value number", "n");
    }

    @Test
    public void testWrongStrictInt() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A value number 'n' is not an integer");
        getInt(getPreparedConfig("n: 1.0"), "n", "value number", "n");
    }

    @Test
    public void testNormalStrictInt() throws Exception {
        assertEquals(50, getInt(getPreparedConfig("n: 50"), "n", "value number", "n"));
    }

    @Test
    public void testEmptyStrictDouble() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty value number 'n'");
        getDouble(getPreparedConfig(""), "n", "value number", "n");
    }

    @Test
    public void testWrongStrictDouble() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A value number 'n' is not a double");
        getDouble(getPreparedConfig("n: 1z.0"), "n", "value number", "n");
    }

    @Test
    public void testNormalStrictDouble() throws Exception {
        assertEquals(50, Math.round(getDouble(getPreparedConfig("n: 50.0"), "n", "value number", "n")));
    }

    @Test
    public void testIntStrictDouble() throws Exception {
        assertEquals(50, Math.round(getDouble(getPreparedConfig("n: 50"), "n", "value number", "n")));
    }

    @Test
    public void testEmptyString() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null or empty string of handler 'x'");
        getString(getPreparedConfig("s:"), "s", "string of handler", "x");
    }

    @Test
    public void testNormalString() throws Exception {
        assertEquals("val", getString(getPreparedConfig("s: val"), "s", "string of handler", "x"));
    }

    @Test
    public void testWrongEnumString() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid com.gmail.uprial.customcreatures.helpers.TestEnum 'T' in enum of handler 'x'");
        getEnumFromString(TestEnum.class, "T", "enum of handler", "x", "");
    }

    @Test
    public void testNormalEnumString() throws Exception {
        assertEquals(TestEnum.A, getEnumFromString(TestEnum.class, "A", "enum of handler", "x", ""));
    }

    @Test
    public void testNormalEnum() throws Exception {
        assertEquals(TestEnum.A, getEnum(TestEnum.class, getPreparedConfig("e: A"), "e", "enum of handler", "x"));
    }

    @Test
    public void testNormalEnumCase() throws Exception {
        assertEquals(TestEnum.A, getEnumFromString(TestEnum.class, "a", "enum of handler", "x", ""));
    }

    @Test
    public void testWrongEnum() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid com.gmail.uprial.customcreatures.helpers.TestEnum 'T' in enum of handler 'x'");
        getEnum(TestEnum.class, getPreparedConfig("e: T"), "e", "enum of handler", "x");
    }

    @Test
    public void testEmptySet() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty set of handler 'x'. Use default value NULL");
        getSet(TestEnum.class, getPreparedConfig(""), getDebugFearingCustomLogger(), "s", "set of handler", "x");
    }

    @Test
    public void testWrongSetType() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid com.gmail.uprial.customcreatures.helpers.TestEnum 'Z' in set of handler 'x' at pos 0");
        getSet(TestEnum.class, getPreparedConfig("x:", "  entities:", "   - Z"), getCustomLogger(),
                "x.entities", "set of handler", "x");
    }

    @Test
    public void testNotUniqueSet() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("com.gmail.uprial.customcreatures.helpers.TestEnum 'A' in handler 'x' is not unique");
        getSet(TestEnum.class, getPreparedConfig("x:", "  entities:", "   - A", "   - A"),
                getCustomLogger(), "x.entities", "handler", "x");
    }

    @Test
    public void testNormalSet() throws Exception {
        //noinspection ConstantConditions
        assertEquals("[A]", getSet(TestEnum.class, getPreparedConfig("entities:", " - A"),
                getParanoiacCustomLogger(), "entities", "path", "entities").toString());
    }

    @Test
    public void testContentOfSet() throws Exception {
        Set<TestEnum> entities = getSet(TestEnum.class, getPreparedConfig("entities:", " - A", " - B"),
                getParanoiacCustomLogger(), "entities", "path", "entities");
        //noinspection ConstantConditions
        assertEquals(2, entities.size());
        assertTrue(entities.contains(TestEnum.A));
        assertTrue(entities.contains(TestEnum.B));
    }
}