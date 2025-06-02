package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HCustomZombieHorse.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HCustomZombieHorseTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyHorse() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty zombie-horse");
        getFromConfig(getPreparedConfig(
                "?:"),
                getParanoiacCustomLogger(), "zh", "zombie-horse");
    }

    @Test
    public void testNoModifications() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No modifications found of zombie-horse");

        getFromConfig(getPreparedConfig(
                "zh:",
                " x: y"), getCustomLogger(), "zh", "zombie-horse");
    }

    @Test
    public void testEmptyJumpStrength() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty jump strength of zombie-horse. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "zh: ",
                "  x: y"), getDebugFearingCustomLogger(), "zh", "zombie-horse");
    }

    @Test
    public void testEmptyTamed() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty 'tamed' flag of zombie-horse. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "zh: ",
                "  jump-strength: 0.7"), getDebugFearingCustomLogger(), "zh", "zombie-horse");
    }

    @Test
    public void testWholeHorse() throws Exception {
        HCustomZombieHorse customHorse = getFromConfig(getPreparedConfig(
                        "zh: ",
                        "  jump-strength: 0.7",
                        "  tamed: false"),
                getParanoiacCustomLogger(), "zh", "zombie-horse");
        assertEquals("Horse{jump-strength: 0.7, tamed: false}", customHorse.toString());
    }
}