package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemAttributes.getFromConfig;
import static org.junit.Assert.assertEquals;
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
        e.expectMessage("Empty max. health multiplier of attributes. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "a: ",
                "  x: y"), getDebugFearingCustomLogger(), "a", "attributes");
    }

    @Test
    public void testWholeAttributes() throws Exception {
        HItemAttributes attributes = getFromConfig(getPreparedConfig(
                "a:",
                "  max-health-multiplier: 1.5"),
                getParanoiacCustomLogger(), "a", "attributes");
        assertEquals("[max-health-multiplier: 1.5]", attributes.toString());
    }

    @Test
    public void testAttributesBC() throws Exception {
        HItemAttributes.saveBackwardCompatibility = true;
        HItemAttributes attributes = getFromConfig(getPreparedConfig(
                "h:",
                " a:",
                "  x, y",
                " max-health: 1.5"),
                getParanoiacCustomLogger(), "h.a", "attributes");
        assertEquals("[max-health-multiplier: 1.5]", attributes.toString());
    }
}