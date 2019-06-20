package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Set;

import static com.gmail.uprial.customcreatures.config.ConfigReaderLists.getItemsList;
import static com.gmail.uprial.customcreatures.config.ConfigReaderSimple.getKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ConfigReaderListsTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    // ==== getItemsList ====
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
                getCustomLogger(), "x", "items list");
        assertNull(items);
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
        assertNotNull(items);
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
        assertNotNull(items);
        assertEquals("[x.z]", items.toString());
    }
}