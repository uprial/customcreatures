package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemArmorTrim.getFromConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HItemArmorTrimTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyTrim() throws Exception {
        HItemArmorTrim itemArmorTrim = HItemArmorTrim.getFromConfig(getPreparedConfig(
                        "?:"),
                getCustomLogger(), "t", "trim");
        assertNull(itemArmorTrim);
    }

    @Test
    public void testNullMaterial() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null material of trim");
        getFromConfig(getPreparedConfig(
                        "t:",
                        "  ?:"),
                getCustomLogger(), "t", "trim");
    }

    @Test
    public void testEmptyMaterial() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null material of trim");
        getFromConfig(getPreparedConfig(
                        "t:",
                        "  material:"),
                getCustomLogger(), "t", "trim");
    }

    @Test
    public void testRandomMaterial() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null pattern of trim");
        getFromConfig(getPreparedConfig(
                        "t:",
                        "  material: RANDOM"),
                getCustomLogger(), "t", "trim");
    }

    @Test
    public void testNullPattern() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null pattern of trim");
        getFromConfig(getPreparedConfig(
                        "t:",
                        "  material: EMERALD"),
                getCustomLogger(), "t", "trim");
    }

    @Test
    public void testEmptyPattern() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null pattern of trim");
        getFromConfig(getPreparedConfig(
                        "t:",
                        "  material: EMERALD",
                        "  pattern:"),
                getCustomLogger(), "t", "trim");
    }

    @Test
    public void testRandomPattern() throws Exception {
        HItemArmorTrim itemArmorTrim = getFromConfig(getPreparedConfig(
                        "t: ",
                        "  material: RANDOM",
                        "  pattern: RANDOM"),
                getParanoiacCustomLogger(), "t", "trim");
        assertEquals("Trim{material: null, pattern: null}", itemArmorTrim.toString());
    }

    @Test
    public void testWholeTrim() throws Exception {
        HItemArmorTrim itemArmorTrim = getFromConfig(getPreparedConfig(
                        "t: ",
                        "  material: EMERALD",
                        "  pattern: SILENCE"),
                getParanoiacCustomLogger(), "t", "trim");
        assertEquals("Trim{material: EMERALD, pattern: SILENCE}", itemArmorTrim.toString());
    }
}