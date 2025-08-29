package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.WolfSelection.getFromConfig;
import static org.junit.Assert.*;


public class WolfSelectionTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyAttributes() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty wolf selection");

        getFromConfig(getPreparedConfig(
                ""), getDebugFearingCustomLogger(), "ws", "wolf selection");
    }

    @Test
    public void testEmptyAttributesValue() throws Exception {
        assertNull(getFromConfig(getPreparedConfig(
                ""), getCustomLogger(), "ws", "wolf selection"));
    }

    @Test
    public void testEmptyRandomizer() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty randomizer of wolf selection");

        getFromConfig(getPreparedConfig(
                "ws: ",
                " x: y"), getCustomLogger(), "ws", "wolf selection");
    }

    @Test
    public void testEmptyBaseArmor() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty base base armor of wolf selection");

        getFromConfig(getPreparedConfig(
                "ws: ",
                " randomizer: 1.0"), getDebugFearingCustomLogger(), "ws", "wolf selection");
    }

    @Test
    public void testEmptyFollowRange() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty base follow range of wolf selection");

        getFromConfig(getPreparedConfig(
                "ws: ",
                " randomizer: 1.0",
                " base-armor:",
                "  base: 1.0",
                "  max: 14.0"), getDebugFearingCustomLogger(), "ws", "wolf selection");
    }

    @Test
    public void testEmptyKnockbackResistance() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty base knockback resistance of wolf selection");

        getFromConfig(getPreparedConfig(
                "ws: ",
                " randomizer: 1.0",
                " base-armor:",
                "  base: 1.0",
                "  max: 14.0",
                " follow-range:",
                "  base: 16.0",
                "  max: 80.0"), getDebugFearingCustomLogger(), "ws", "wolf selection");
    }

    @Test
    public void testEmptyMovementSpeed() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty base movement speed of wolf selection");

        getFromConfig(getPreparedConfig(
                "ws: ",
                " randomizer: 1.0",
                " base-armor:",
                "  base: 1.0",
                "  max: 14.0",
                " follow-range:",
                "  base: 16.0",
                "  max: 80.0",
                " knockback-resistance:",
                "  base: 0.0",
                "  max: 0.8"), getDebugFearingCustomLogger(), "ws", "wolf selection");
    }

    @Test
    public void testEmptyScale() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty base scale of wolf selection");

        getFromConfig(getPreparedConfig(
                "ws: ",
                " randomizer: 1.0",
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
                "  max: 0.54"), getDebugFearingCustomLogger(), "ws", "wolf selection");
    }

    @Test
    public void testWholeAttributes() throws Exception {
        WolfSelection wolfSelection = getFromConfig(getPreparedConfig(
                        "ws: ",
                        " randomizer:",
                        "  type: random",
                        "  min: 0.8",
                        "  max: 1.2",
                        "  distribution: normal",
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
                        "  max: 3.0"),
                getParanoiacCustomLogger(), "ws", "wolf selection");
        assertNotNull(wolfSelection);
        assertEquals("{randomizer: DoubleValueRandom{distribution: NORMAL, min: 0.80, max: 1.20}," +
                " base-armor: {base: 1, max: 14}," +
                " follow-range: {base: 16, max: 80}," +
                " knockback-resistance: {base: 0, max: 0.8}," +
                " movement-speed: {base: 0.3, max: 0.54}," +
                " scale: {base: 1, max: 3}}", wolfSelection.toString());
    }
}