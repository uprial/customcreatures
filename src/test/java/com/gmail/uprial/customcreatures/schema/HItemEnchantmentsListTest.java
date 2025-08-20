package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemEnchantmentsList.getFromConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
                "  schedules:",
                "   - s1",
                "e2:",
                "  type: LUCK_OF_THE_SEA",
                "  level: 1",
                "  schedules:",
                "   - s1",
                "s1:",
                "  timezone: Europe/London",
                "  days-of-the-week:",
                "  - MON"),
                getParanoiacCustomLogger(), "ee", "enchantments list");
        assertNotNull(itemEnchantmentsList);
        assertEquals("[{type: THORNS, level: 1, schedules: [{timezone: Europe/London, days-of-the-week: [MON]}]}," +
                " {type: LUCK_OF_THE_SEA, level: 1, schedules: [{timezone: Europe/London, days-of-the-week: [MON]}]}]", itemEnchantmentsList.toString());
    }

    @Test
    public void testEmptyEnchantmentsValue() throws Exception {
        HItemEnchantmentsList itemEnchantmentsList = getFromConfig(getPreparedConfig(
                "ee:"),
                getCustomLogger(), "ee", "enchantments list");
        assertNull(itemEnchantmentsList);
    }
}