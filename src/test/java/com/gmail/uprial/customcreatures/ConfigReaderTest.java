package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.bukkit.entity.EntityType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

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
    public void testWrongSetType() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid org.bukkit.entity.EntityType 'Z' in handler 'x' at pos 0");
        getSet(EntityType.class, getPreparedConfig("x:", "  entities:", "   - Z"), getCustomLogger(),
                "x.entities", "handler", "x");
    }

    @Test
    public void testNotUniqueSet() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("org.bukkit.entity.EntityType 'ZOMBIE' in handler 'x' is not unique");
        getSet(EntityType.class, getPreparedConfig("x:", "  entities:", "   - ZOMBIE", "   - ZOMBIE"),
                getCustomLogger(), "x.entities", "handler", "x");
    }

    @Test
    public void testNormalSet() throws Exception {
        assertEquals("[ZOMBIE]", getSet(EntityType.class, getPreparedConfig("entities:", " - ZOMBIE"),
                getParanoiacCustomLogger(), "entities", "path", "entities").toString());
    }

    @Test
    public void testAnotherNormalSet() throws Exception {
        assertEquals("[ZOMBIE, PIG]", getSet(EntityType.class, getPreparedConfig("entities:", " - ZOMBIE", " - PIG"),
                getParanoiacCustomLogger(), "entities", "path", "entities").toString());
    }
}