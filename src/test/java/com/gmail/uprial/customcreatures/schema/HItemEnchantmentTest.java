package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemEnchantment.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HItemEnchantmentTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeEnchantment() throws Exception {
        HItemEnchantment itemEnchantment = getFromConfig(getPreparedConfig(
                "e:",
                "  type: THORNS",
                "  level: 1"),
                getParanoiacCustomLogger(), "e", "enchantment");
        assertEquals("{type: THORNS, level: 1}", itemEnchantment.toString());
    }

    @Test
    public void testEmptyEnchantment() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty enchantment");
        getFromConfig(getPreparedConfig(
                "?:"),
                getParanoiacCustomLogger(), "e", "enchantment");
    }

    @Test
    public void testEmptyType() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null enchantment type of enchantment");
        getFromConfig(getPreparedConfig(
                "e:",
                "  type:"),
                getParanoiacCustomLogger(), "e", "enchantment");
    }

    @Test
    public void testEmptyLevel() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty level of enchantment");
        getFromConfig(getPreparedConfig(
                "e:",
                "  type: THORNS"),
                getCustomLogger(), "e", "enchantment");
    }
}