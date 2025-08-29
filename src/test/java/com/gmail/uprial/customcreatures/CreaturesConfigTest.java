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
    public void testEmpty() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty 'timeout-in-ms' value. Use default value 5");
        loadConfig(getDebugFearingCustomLogger(), "");
    }

    @Test
    public void testNotMap() throws Exception {
        e.expect(InvalidConfigurationException.class);
        e.expectMessage("Top level is not a Map.");
        loadConfig("x");
    }

    @Test
    public void testWrongTimeoutInS() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A 'timeout-in-ms' value is not an integer");
        loadConfig(
                "timeout-in-ms: v");
    }

    @Test
    public void testNoHandlers() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty 'breeders' list");
        loadConfig("x:");
    }

    @Test
    public void testEmptyHandlersList() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty 'breeders' list");
        loadConfig("breeders:");
    }

    @Test
    public void testNoDefinitionOfHandlers() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null definition of 'x' for 'breeders' at pos 0");
        loadConfig("breeders:",
                " - x");
    }

    @Test
    public void testDuplicateKeyInHandlers() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Key 'x' in 'breeders' is not unique");
        loadConfig("breeders:",
                " - x",
                " - x",
                "x:",
                " filter:",
                "  types:",
                "   - ZOMBIE",
                " randomizer: 1.0");
    }

    @Test
    public void testDuplicateKeyInBreederssWithDifferentCase() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Key 'X' in 'breeders' is not unique");
        loadConfig("breeders:",
                " - x",
                " - X",
                "x:",
                " filter:",
                "  types:",
                "   - ZOMBIE",
                " randomizer: 1.0");
    }

    @Test
    public void testNoValidHandlersDefinitions() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("There are no valid 'breeders' definitions");
        loadConfig(getIndifferentCustomLogger(), "breeders:",
                " - x",
                "x:",
                " filter:",
                "  types:",
                "   - ZOMBIE");
    }

    @Test
    public void testNormalConfig() throws Exception {
        assertEquals(
                "timeout-in-ms: 5," +
                        " breeders: [{filter: {types: [WOLF], exclude-types: null," +
                        " type-sets: null, exclude-type-sets: null}," +
                        " randomizer: 1.01," +
                        " base-armor: {base: 1, max: 14}," +
                        " follow-range: {base: 16, max: 80}," +
                        " knockback-resistance: {base: 0, max: 0.8}," +
                        " movement-speed: {base: 0.3, max: 0.54}," +
                        " scale: {base: 1, max: 3}}]," +
                        " handlers: [{name: x, filter: {types: [ZOMBIE], exclude-types: null," +
                        " type-sets: null, exclude-type-sets: null," +
                        " reasons: null, exclude-reasons: null, probability: null," +
                        " probability-player-multiplier: null}," +
                        " effects: null, attributes: {max-health-multiplier: 1.0," +
                        " base-armor: null, follow-range: null, knockback-resistance: null," +
                        " max-health: null, movement-speed-multiplier: null, movement-speed: null, scale: null," +
                        " remove-when-far-away: null}," +
                        " equipment: null, drops: null, drop-exp: null, entity-specific-attributes: null," +
                        " spawn: null}]",
                loadConfig("timeout-in-ms: 5",
                        "breeders:",
                        " - wolf-breeder",
                        "wolf-breeder:",
                        " filter:",
                        "  types:",
                        "   - WOLF",
                        " randomizer: 1.01",
                        " base-armor:",
                        "  base: 1.0",
                        "  max: 14.0",
                        " follow-range:",
                        "  base: 16.0",
                        "  max: 80.0",
                        " knockback-resistance:",
                        "  base: 0.0",
                        "  max: 0.8",
                        " movement-speed:",
                        "  base: 0.3",
                        "  max: 0.54",
                        " scale:",
                        "  base: 1.0",
                        "  max: 3.0",
                        "handlers:",
                        " - x",
                        "x:",
                        " filter:",
                        "  types:",
                        "   - ZOMBIE",
                        " attributes:",
                        "   max-health-multiplier: 1.0").toString());
    }
}