package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HCustomCreeper.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HCustomCreeperTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyCreeper() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty creeper");
        getFromConfig(getPreparedConfig(
                "?:"),
                getParanoiacCustomLogger(), "c", "creeper");
    }

    @Test
    public void testNoModifications() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No modifications found of creeper");

        getFromConfig(getPreparedConfig(
                "c:",
                " x: y"), getCustomLogger(), "c", "creeper");
    }

    @Test
    public void testEmptyMaxFuseTicks() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty max. fuse ticks of creeper. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "c: ",
                "  x: y"), getDebugFearingCustomLogger(), "c", "creeper");
    }

    @Test
    public void testEmptyExplosionRadius() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty explosion radius of creeper. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "c: ",
                "  max-fuse-ticks: 10"), getDebugFearingCustomLogger(), "c", "creeper");
    }

    @Test
    public void testEmptyPowered() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty 'powered' flag of creeper. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "c: ",
                "  max-fuse-ticks: 10",
                "  explosion-radius: 1"), getDebugFearingCustomLogger(), "c", "creeper");
    }

    @Test
    public void testWholeCreeper() throws Exception {
        HCustomCreeper customCreeper = getFromConfig(getPreparedConfig(
                        "c:",
                        "  max-fuse-ticks: 10",
                        "  explosion-radius: 1",
                        "  powered: true"),
                getParanoiacCustomLogger(), "c", "creeper");
        assertEquals("Creeper{max-fuse-ticks: 10, explosion-radius: 1, powered: true}", customCreeper.toString());
    }
}