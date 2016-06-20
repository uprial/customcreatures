package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CreaturesConfigTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testNotMap() throws Exception {
        e.expect(InvalidConfigurationException.class);
        e.expectMessage("Top level is not a Map.");
        loadConfig("x");
    }

    @Test
    public void testEmptyDebug() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty value flag 'debug'. Use default value false");
        loadConfig(getDebugFearingCustomLogger(), "");
    }

    @Test
    public void testNoHandlers() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty 'handlers' list");
        loadConfig("x:");
    }

    @Test
    public void testEmptyHandlersList() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty 'handlers' list");
        loadConfig("handlers:");
    }

    @Test
    public void testNullKeyInHandlers() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null key in 'handlers' at pos 0");
        loadConfig("handlers:",
                " -");
    }

    @Test
    public void testEmptyKeyInHandlers() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty key in 'handlers' at pos 0");
        loadConfig("handlers:",
                " - ''");
    }

    @Test
    public void testNoDefinitionOfHandlers() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null definition of handler 'x' at pos 0");
        loadConfig("handlers:",
                " - x");
    }

    @Test
    public void testDuplicateKeyInHandlers() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Key 'x' in 'handlers' is not unique");
        loadConfig("handlers:",
                " - x",
                " - x",
                "x: y");
    }

    @Test
    public void testDuplicateKeyInHandlersWithDifferentCase() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Key 'X' in 'handlers' is not unique");
        loadConfig("handlers:",
                " - x",
                " - X",
                "x: y");
    }

    @Test
    public void testNoValidHandlersDefinitions() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("There are no valid handlers definitions");
        loadConfig("handlers:",
                " - x",
                "x:",
                " filter: dd");
    }
}