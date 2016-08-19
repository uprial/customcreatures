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
    public void testWholeAttributes() throws Exception {
        HItemAttributes attributes = getFromConfig(getPreparedConfig(
                "a:",
                " base-armor: 1.0",
                " attack-damage: 10.0",
                " follow-range: 50.1",
                " knockback-resistance: 10.0",
                " max-health: 10.0",
                " max-health-multiplier: 0.1",
                " movement-speed: 10.0"),
                getParanoiacCustomLogger(), "a", "attributes");
        assertNotNull(attributes);
        assertEquals("[max-health-multiplier: 0.1, base-armor: 1.0, attack-damage: 10.0," +
                " follow-range: 50.1, knockback-resistance: 10.0, max-health: 10.0, movement-speed: 10.0]", attributes.toString());
    }

    @Test
    public void testAttributesBCWarning() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("[WARNING] Property 'h.a.max-health' of deprecated, please use 'h.a.max-health-multiplier");

        HItemAttributes.setBackwardCompatibility(true);
        try {
            getFromConfig(getPreparedConfig(
                    "h:",
                    " a:",
                    "  x, y",
                    " max-health: 1.5"),
                    getDebugFearingCustomLogger(), "h.a", "attributes");
        } finally {
            HItemAttributes.setBackwardCompatibility(false);
        }
    }

    @Test
    public void testAttributesBC() throws Exception {
        HItemAttributes.setBackwardCompatibility(true);
        try {
            HItemAttributes attributes = getFromConfig(getPreparedConfig(
                    "h:",
                    " a:",
                    "  x, y",
                    " max-health: 1.5"),
                    getIndifferentCustomLogger(), "h.a", "attributes");
            assertNotNull(attributes);
            assertEquals("[max-health-multiplier: 1.5, base-armor: null, attack-damage: null, " +
                    "follow-range: null, knockback-resistance: null, max-health: null, movement-speed: null]", attributes.toString());
        } finally {
            HItemAttributes.setBackwardCompatibility(false);
        }
    }
}