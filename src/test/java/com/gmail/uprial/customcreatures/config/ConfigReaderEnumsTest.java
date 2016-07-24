package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import com.gmail.uprial.customcreatures.helpers.TestEnum;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.Set;

import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.*;
import static org.junit.Assert.*;

public class ConfigReaderEnumsTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

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
        assertNull(getSet(TestEnum.class, getPreparedConfig(""), getCustomLogger(), "s", "set"));
    }

    @Test
    public void testWrongSetType() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid com.gmail.uprial.customcreatures.helpers.TestEnum 'Z' in set at pos 0");
        getSet(TestEnum.class, getPreparedConfig("x:", "  entities:", "   - Z"), getParanoiacCustomLogger(),
                "x.entities", "set");
    }

    @Test
    public void testNotUniqueSet() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("com.gmail.uprial.customcreatures.helpers.TestEnum 'A' in set is not unique");
        getSet(TestEnum.class, getPreparedConfig("x:", "  entities:", "   - A", "   - A"),
                getParanoiacCustomLogger(), "x.entities", "set");
    }

    @Test
    public void testNormalSet() throws Exception {
        Set<TestEnum> set = getSet(TestEnum.class, getPreparedConfig("entities:", " - A"),
                getParanoiacCustomLogger(), "entities", "path");
        assertNotNull(set);
        assertEquals("[A]", set.toString());
    }

    @Test
    public void testContentOfSet() throws Exception {
        Set<TestEnum> entities = getSet(TestEnum.class, getPreparedConfig("entities:", " - A", " - B"),
                getParanoiacCustomLogger(), "entities", "path");
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertTrue(entities.contains(TestEnum.A));
        assertTrue(entities.contains(TestEnum.B));
    }
}