package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemDrop.getFromConfig;
import static org.junit.Assert.*;

public class HItemDropTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeItemDrop() throws Exception {
        HItemDrop itemDrop = getFromConfig(getPreparedConfig(
                "d:",
                "  probability: 100",
                "  probability-per-looting-level: 1",
                "  durability: 100",
                "  material: DIAMOND_SWORD",
                "  amount:",
                "    type: random",
                "    distribution: NORMAL",
                "    min: 1",
                "    max: 2",
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
                getParanoiacCustomLogger(), "d", "item drop");
        assertNotNull(itemDrop);
        assertEquals("{probability: null, probability-per-looting-level: 1.0, material: DIAMOND_SWORD," +
                        " amount: IntValueRandom{distribution: NORMAL, min: 1, max: 2}, amount-max-per-looting-level: 1," +
                        " enchantments: [{type: PROTECTION, level: 2, schedules: [{timezone: Europe/London, days-of-the-week: [MON]}]}]," +
                        " durability: 100, trim: Trim{material: EMERALD, pattern: SILENCE}," +
                        " schedules: [{timezone: Europe/London, days-of-the-week: [MON]}]}",
                itemDrop.toString());
    }

    @Test
    public void testWholeItemDropWithoutDefaults() throws Exception {
        HItemDrop itemDrop = getFromConfig(getPreparedConfig(
                "d:",
                "  probability: 50",
                "  probability-per-looting-level: 1",
                "  material: DIAMOND_SWORD",
                "  amount: 10",
                "  amount-max-per-looting-level: 1",
                "  enchantments:",
                "   - e",
                "  trim:",
                "    material: EMERALD",
                "    pattern: SILENCE",
                "  e:",
                "    type: THORNS",
                "    level: 1",
                "    schedules:",
                "     - s1",
                "  durability: 40",
                "  schedules:",
                "   - s1",
                "s1:",
                "  timezone: Europe/London",
                "  days-of-the-week:",
                "  - MON"),
                getParanoiacCustomLogger(), "d", "item drop");
        assertNotNull(itemDrop);
        assertEquals("{probability: 50, probability-per-looting-level: 1.0, material: DIAMOND_SWORD," +
                        " amount: IntValueRandom{distribution: NORMAL, min: 10, max: 10}, amount-max-per-looting-level: 1," +
                        " enchantments: [{type: THORNS, level: 1, schedules: [{timezone: Europe/London, days-of-the-week: [MON]}]}]," +
                        " durability: 40, trim: Trim{material: EMERALD, pattern: SILENCE}," +
                        " schedules: [{timezone: Europe/London, days-of-the-week: [MON]}]}",
                itemDrop.toString());
    }

    @Test
    public void testWholeItemDropWithDefaults() throws Exception {
        HItemDrop itemDrop = getFromConfig(getPreparedConfig(
                        "d:",
                        "  material: DIAMOND_SWORD"),
                getCustomLogger(), "d", "item drop");
        assertNotNull(itemDrop);
        assertEquals("{probability: null, probability-per-looting-level: 0.0, material: DIAMOND_SWORD," +
                        " amount: IntValueRandom{distribution: NORMAL, min: 1, max: 1}, amount-max-per-looting-level: 0," +
                        " enchantments: null, durability: null, trim: null, schedules: []}",
                itemDrop.toString());
    }

    @Test
    public void testEmptyItemDrop() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty item drop");
        getFromConfig(getPreparedConfig(
                "?:"),
                getCustomLogger(), "d", "item drop");
    }

    @Test
    public void testEmptyProbability() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty probability of item drop. Use default value 100");
        getFromConfig(getPreparedConfig(
                "d:",
                " k: v"),
                getDebugFearingCustomLogger(), "d", "item drop");
    }

    @Test
    public void testEmptyMaterial() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null material of item drop");
        getFromConfig(getPreparedConfig(
                "d:",
                " k: v"),
                getCustomLogger(), "d", "item drop");
    }

    @Test
    public void testEmptyProbabilityPerLootingLevel() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty probability per looting level of item drop. Use default value NULL");
        getFromConfig(getPreparedConfig(
                        "d:",
                        " probability: 50"),
                getDebugFearingCustomLogger(), "d", "item drop");
    }
    @Test
    public void testEmptyAmount() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty amount of item drop. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "d:",
                " probability: 50",
                " probability-per-looting-level: 1",
                " material: DIAMOND_SWORD"),
                getDebugFearingCustomLogger(), "d", "item drop");
    }

    @Test
    public void testEmptyAmountMaxPerLootingLevel() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty amount max per looting level of item drop. Use default value NULL");
        getFromConfig(getPreparedConfig(
                        "d:",
                        " probability: 50",
                        " probability-per-looting-level: 1",
                        " material: DIAMOND_SWORD",
                        " amount: 1"),
                getDebugFearingCustomLogger(), "d", "item drop");
    }

    @Test
    public void testEmptyDurability() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty durability of item drop");
        getFromConfig(getPreparedConfig(
                "d:",
                " probability: 77",
                " probability-per-looting-level: 1",
                " material: DIAMOND_SWORD",
                " amount: 1",
                " amount-max-per-looting-level: 1"),
                getDebugFearingCustomLogger(), "d", "item drop");
    }

    @Test
    public void testEmptyEnchantments() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty enchantments of item drop");
        getFromConfig(getPreparedConfig(
                "d:",
                " probability: 77",
                " probability-per-looting-level: 1",
                " material: DIAMOND_SWORD",
                " amount: 1",
                " amount-max-per-looting-level: 1",
                " durability: 77"),
                getDebugFearingCustomLogger(), "d", "item drop");
    }

    @Test
    public void testEmptyArmorTrim() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty trim of item drop. Use default value NULL");
        getFromConfig(getPreparedConfig(
                        "d:",
                        " probability: 77",
                        " probability-per-looting-level: 1",
                        " material: DIAMOND_SWORD",
                        " amount: 1",
                        " amount-max-per-looting-level: 1",
                        " durability: 77",
                        " enchantments:",
                        "  - e1",
                        "e1:",
                        " type: PROTECTION",
                        " level: 2",
                        " schedules:",
                        "  - s1",
                        "s1:",
                        "  timezone: Europe/London",
                        "  days-of-the-week:",
                        "  - MON"),
                getDebugFearingCustomLogger(), "d", "item drop");
    }
}