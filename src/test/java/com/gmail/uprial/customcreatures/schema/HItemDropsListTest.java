package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemDropsList.getFromConfig;
import static org.junit.Assert.*;

public class HItemDropsListTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeEffectsList() throws Exception {
        HItemDropsList itemDropsList = getFromConfig(getPreparedConfig(
                "dd:",
                "  - d",
                "d:",
                "  probability: 100",
                "  probability-per-looting-level: 1",
                "  durability: 100",
                "  material: DIAMOND_SWORD",
                "  amount: 1",
                "  amount-max-per-looting-level: 1",
                "  enchantments:",
                "   - e1",
                "  trim:",
                "    material: EMERALD",
                "    pattern: SILENCE",
                "  schedules:",
                "   - s1",
                "e1:",
                " type: PROTECTION",
                " level: 2",
                " schedules:",
                "   - s1",
                "s1:",
                "  timezone: Europe/London",
                "  days-of-the-week:",
                "  - MON"),
                getParanoiacCustomLogger(), "dd", "drops list");
        assertNotNull(itemDropsList);
        assertEquals("[{probability: null, probability-per-looting-level: 1.0, material: DIAMOND_SWORD," +
                " amount: IntValueRandom{distribution: NORMAL, min: 1, max: 1}, amount-max-per-looting-level: 1," +
                " enchantments: [{type: PROTECTION, level: 2, schedules: [{timezone: Europe/London, days-of-the-week: [MON]}]}]," +
                " durability: 100, trim: Trim{material: EMERALD, pattern: SILENCE}," +
                " schedules: [{timezone: Europe/London, days-of-the-week: [MON]}]}]", itemDropsList.toString());
    }

    @Test
    public void testEmptyDrops() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty item drops");
        getFromConfig(getPreparedConfig(
                        "?:"),
                getDebugFearingCustomLogger(), "dd", "item drops");
    }

    @Test
    public void testEmptyDropsValue() throws Exception {
        assertNull(getFromConfig(getPreparedConfig(
                        "?:"),
                getCustomLogger(), "dd", "item drops"));
    }
}