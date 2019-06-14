package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class CreaturesConfigTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyDebug() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty 'debug' flag. Use default value false");
        CreaturesConfig.isDebugMode(getPreparedConfig(""), getDebugFearingCustomLogger());
    }

    @Test
    public void testNormalDebug() throws Exception {
        assertTrue(CreaturesConfig.isDebugMode(getPreparedConfig("debug: true"), getDebugFearingCustomLogger()));
    }

    @Test
    public void testEmptyFixProjectileTrajectory() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty 'fix-projectile-trajectory' flag. Use default value true");
        loadConfig(getDebugFearingCustomLogger(), "");
    }

    @Test
    public void testEmpty() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty 'handlers' list");
        loadConfig(getDebugFearingCustomLogger(), "fix-projectile-trajectory: true");
    }

    @Test
    public void testNotMap() throws Exception {
        e.expect(InvalidConfigurationException.class);
        e.expectMessage("Top level is not a Map.");
        loadConfig("x");
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
                "x:",
                " filter:",
                "   probability: 99",
                " attributes:",
                "   max-health-multiplier: 1.0");
    }

    @Test
    public void testDuplicateKeyInHandlersWithDifferentCase() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Key 'X' in 'handlers' is not unique");
        loadConfig("handlers:",
                " - x",
                " - X",
                "x:",
                " filter:",
                "  probability: 99",
                " attributes:",
                "   max-health-multiplier: 1.0");
    }

    @Test
    public void testNoValidHandlersDefinitions() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("There are no valid handlers definitions");
        loadConfig(getIndifferentCustomLogger(), "handlers:",
                " - x",
                "x:",
                " filter: dd");
    }

    @Test
    public void testNormalConfig() throws Exception {
        assertEquals(
                "fix-projectile-trajectory: true, " +
                        "handlers: [[name: x, filter: [types: null, type-sets: null, reasons: null, probability: 99], " +
                        "effects: null, attributes: [max-health-multiplier: 1.0, attack-damage-multiplier: null, " +
                        "base-armor: null, follow-range: null, knockback-resistance: null," +
                        " max-health: null, movement-speed-multiplier: null], equipment: null]]",
                loadConfig("handlers:",
                        " - x",
                        "x:",
                        " filter:",
                        "   probability: 99",
                        " attributes:",
                        "   max-health-multiplier: 1.0").toString());
    }
}