package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemAttributes.getFromConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class HItemAttributesTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyAttributes() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty attributes");

        getFromConfig(getPreparedConfig(
                ""), getDebugFearingCustomLogger(), "a", "attributes");
    }

    @Test
    public void testEmptyAttributesValue() throws Exception {
        assertNull(getFromConfig(getPreparedConfig(
                ""), getCustomLogger(), "a", "attributes"));
    }

    @Test
    public void testNoModifications() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No modifications found in attributes");

        getFromConfig(getPreparedConfig(
                "a:",
                " x: y"), getCustomLogger(), "a", "attributes");
    }

    @Test
    public void testEmptyMaxHealthMultiplier() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty max. health multiplier in attributes. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "a: ",
                "  x: y"), getDebugFearingCustomLogger(), "a", "attributes");
    }

    @Test
    public void testEmptyBaseArmor() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty base armor in attributes. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "a: ",
                " max-health-multiplier: 0.1"), getDebugFearingCustomLogger(), "a", "attributes");
    }

    @Test
    public void testEmptyFollowRange() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty follow range in attributes. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "a: ",
                " max-health-multiplier: 0.1",
                " base-armor: 1.0"), getDebugFearingCustomLogger(), "a", "attributes");
    }

    @Test
    public void testEmptyKnockbackResistance() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty knockback resistance in attributes. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "a: ",
                " max-health-multiplier: 0.1",
                " base-armor: 1.0",
                " follow-range: 50.1"), getDebugFearingCustomLogger(), "a", "attributes");
    }

    @Test
    public void testEmptyMaxHealth() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty max. health in attributes. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "a: ",
                " max-health-multiplier: 0.1",
                " base-armor: 1.0",
                " follow-range: 50.1",
                " knockback-resistance: 1.0"), getDebugFearingCustomLogger(), "a", "attributes");
    }

    @Test
    public void testEmptyMovementSpeedMultiplier() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty movement speed multiplier in attributes. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "a: ",
                " max-health-multiplier: 0.1",
                " base-armor: 1.0",
                " follow-range: 50.1",
                " knockback-resistance: 1.0",
                " max-health: 10.0"), getDebugFearingCustomLogger(), "a", "attributes");
    }

    @Test
    public void testEmptyMovementSpeed() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty movement speed in attributes. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "a: ",
                " max-health-multiplier: 0.1",
                " base-armor: 1.0",
                " follow-range: 50.1",
                " knockback-resistance: 1.0",
                " max-health: 10.0",
                " movement-speed-multiplier: 10.0"), getDebugFearingCustomLogger(), "a", "attributes");
    }

    @Test
    public void testEmptyScale() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty scale in attributes. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "a: ",
                " max-health-multiplier: 0.1",
                " base-armor: 1.0",
                " follow-range: 50.1",
                " knockback-resistance: 1.0",
                " max-health: 10.0",
                " movement-speed-multiplier: 10.0",
                " movement-speed: 0.5"), getDebugFearingCustomLogger(), "a", "attributes");
    }

    @Test
    public void testEmptyRemoveWhenFarAway() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty 'remove when far away' flag in attributes. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "a: ",
                " max-health-multiplier: 0.1",
                " base-armor: 1.0",
                " follow-range: 50.1",
                " knockback-resistance: 1.0",
                " max-health: 10.0",
                " movement-speed-multiplier: 10.0",
                " movement-speed: 0.5",
                " scale: 1.0"), getDebugFearingCustomLogger(), "a", "attributes");
    }

    @Test
    public void testWholeAttributes() throws Exception {
        HItemAttributes attributes = getFromConfig(getPreparedConfig(
                "a:",
                " max-health-multiplier: 0.1",
                " base-armor: 1.0",
                " follow-range: 50.1",
                " knockback-resistance: 1.0",
                " max-health: 10.0",
                " movement-speed-multiplier: 10.0",
                " movement-speed: 0.5",
                " scale: 1.0",
                " remove-when-far-away: true"),
                getParanoiacCustomLogger(), "a", "attributes");
        assertNotNull(attributes);
        assertEquals("{max-health-multiplier: 0.1, base-armor: 1.0," +
                " follow-range: 50.1, knockback-resistance: 1.0, max-health: 10.0," +
                " movement-speed-multiplier: 10.0, movement-speed: 0.5, scale: 1.0," +
                " remove-when-far-away: true}", attributes.toString());
    }
}