package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.Breeder.getFromConfig;
import static org.junit.Assert.*;

public class BreederTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyConfig() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty filter of breeder");

        getFromConfig(getPreparedConfig(
                ""), getDebugFearingCustomLogger(), "b", "breeder");
    }

    @Test
    public void testEmptyFilter() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty filter of breeder");

        getFromConfig(getPreparedConfig(
                "b: ",
                " x: y"), getDebugFearingCustomLogger(), "b", "breeder");
    }

    @Test
    public void testEmptyRandomizer() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty randomizer of breeder");

        getFromConfig(getPreparedConfig(
                "b: ",
                " filter:",
                "  types:",
                "    - COW"), getCustomLogger(), "b", "breeder");
    }

    @Test
    public void testEmptyBaseArmor() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty base armor of breeder. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "b: ",
                " filter:",
                "  types:",
                "    - COW",
                "  exclude-types:",
                "    - HUSK",
                "  type-sets:",
                "    - MONSTERS",
                "  exclude-type-sets:",
                "    - SKELETONS",
                " randomizer: 1.0"), getDebugFearingCustomLogger(), "b", "breeder");
    }

    @Test
    public void testEmptyFollowRange() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty follow range of breeder. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "b: ",
                " filter:",
                "  types:",
                "    - COW",
                "  exclude-types:",
                "    - HUSK",
                "  type-sets:",
                "    - MONSTERS",
                "  exclude-type-sets:",
                "    - SKELETONS",
                " randomizer: 1.0",
                " base-armor:",
                "  base: 1.0",
                "  max: 14.0"), getDebugFearingCustomLogger(), "b", "breeder");
    }

    @Test
    public void testEmptyKnockbackResistance() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty knockback resistance of breeder. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "b: ",
                " filter:",
                "  types:",
                "    - COW",
                "  exclude-types:",
                "    - HUSK",
                "  type-sets:",
                "    - MONSTERS",
                "  exclude-type-sets:",
                "    - SKELETONS",
                " randomizer: 1.0",
                " base-armor:",
                "  base: 1.0",
                "  max: 14.0",
                " follow-range:",
                "  base: 16.0",
                "  max: 80.0"), getDebugFearingCustomLogger(), "b", "breeder");
    }

    @Test
    public void testEmptyMovementSpeed() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty movement speed of breeder. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "b: ",
                " filter:",
                "  types:",
                "    - COW",
                "  exclude-types:",
                "    - HUSK",
                "  type-sets:",
                "    - MONSTERS",
                "  exclude-type-sets:",
                "    - SKELETONS",
                " randomizer: 1.0",
                " base-armor:",
                "  base: 1.0",
                "  max: 14.0",
                " follow-range:",
                "  base: 16.0",
                "  max: 80.0",
                " knockback-resistance:",
                "  base: 0.0",
                "  max: 0.8"), getDebugFearingCustomLogger(), "b", "breeder");
    }

    @Test
    public void testEmptyScale() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty scale of breeder. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "b: ",
                " filter:",
                "  types:",
                "    - COW",
                "  exclude-types:",
                "    - HUSK",
                "  type-sets:",
                "    - MONSTERS",
                "  exclude-type-sets:",
                "    - SKELETONS",
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
                "  max: 0.54"), getDebugFearingCustomLogger(), "b", "breeder");
    }

    @Test
    public void testWholeAttributes() throws Exception {
        Breeder breeder = getFromConfig(getPreparedConfig(
                        "b: ",
                        " filter:",
                        "  types:",
                        "    - COW",
                        "  exclude-types:",
                        "    - HUSK",
                        "  type-sets:",
                        "    - MONSTERS",
                        "  exclude-type-sets:",
                        "    - SKELETONS",
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
                getParanoiacCustomLogger(), "b", "breeder");
        assertNotNull(breeder);
        assertEquals("{filter: {types: [COW], exclude-types: [HUSK]," +
                " type-sets: [MONSTERS], exclude-type-sets: [SKELETONS]}," +
                " randomizer: DoubleValueRandom{distribution: NORMAL, min: 0.80, max: 1.20}," +
                " base-armor: {base: 1, max: 14}," +
                " follow-range: {base: 16, max: 80}," +
                " knockback-resistance: {base: 0, max: 0.8}," +
                " movement-speed: {base: 0.3, max: 0.54}," +
                " scale: {base: 1, max: 3}}", breeder.toString());
    }

    @Test
    public void testPartialAttributes() throws Exception {
        Breeder breeder = getFromConfig(getPreparedConfig(
                        "b: ",
                        " filter:",
                        "  types:",
                        "    - WOLF",
                        " randomizer:",
                        "  type: random",
                        "  min: 0.8",
                        "  max: 1.2",
                        "  distribution: normal",
                        " base-armor:",
                        "  base: 1.0",
                        "  max: 14.0",
                        " scale:",
                        "  base: 1.0",
                        "  max: 3.0"),
                getCustomLogger(), "b", "breeder");
        assertNotNull(breeder);
        assertEquals("{filter: {types: [WOLF], exclude-types: null," +
                " type-sets: null, exclude-type-sets: null}," +
                " randomizer: DoubleValueRandom{distribution: NORMAL, min: 0.80, max: 1.20}," +
                " base-armor: {base: 1, max: 14}," +
                " follow-range: null," +
                " knockback-resistance: null," +
                " movement-speed: null," +
                " scale: {base: 1, max: 3}}", breeder.toString());
    }
}