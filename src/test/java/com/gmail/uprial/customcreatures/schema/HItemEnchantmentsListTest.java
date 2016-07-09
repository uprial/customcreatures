package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemEnchantmentsList.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HItemEnchantmentsListTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeEnchantmentsList() throws Exception {
        HItemEnchantmentsList itemEnchantmentsList = getFromConfig(getPreparedConfig(
                "ee:",
                "  - e1",
                "  - e2",
                "e1:",
                "  type: THORNS",
                "  level: 1",
                "e2:",
                "  type: LUCK",
                "  level: 1"),
                getParanoiacCustomLogger(), "ee", "enchantments list");
        //noinspection ConstantConditions
        assertEquals("{[type: THORNS, level: 1], [type: LUCK, level: 1]}", itemEnchantmentsList.toString());
    }

    @Test
    public void testEmptyEnchantmentsValue() throws Exception {
        HItemEnchantmentsList itemEnchantmentsList = getFromConfig(getPreparedConfig(
                "ee:"),
                getParanoiacCustomLogger(), "ee", "enchantments list");
        assertEquals(null, itemEnchantmentsList);
    }
}