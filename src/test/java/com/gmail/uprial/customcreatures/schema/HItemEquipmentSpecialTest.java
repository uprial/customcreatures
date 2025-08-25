package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemEquipmentSpecial.getFromConfig;
import static com.gmail.uprial.customcreatures.schema.EquipmentType.BODY;
import static org.junit.Assert.*;

public class HItemEquipmentSpecialTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeEquipmentSpecial() throws Exception {
        HItemEquipmentSpecial equipmentSpecial = getFromConfig(getPreparedConfig(
                "es:",
                "  probability: 100",
                "  material: DIAMOND_HORSE_ARMOR",
                "  durability: 100",
                "  enchantments:",
                "   - e1",
                "e1:",
                " type: PROTECTION",
                " level: 2",
                " schedules:",
                "   - s1",
                "s1:",
                "  timezone: Europe/London",
                "  days-of-the-week:",
                "  - MON"),
                getParanoiacCustomLogger(), BODY, "es", "equipment special");
        assertNotNull(equipmentSpecial);
        assertEquals("{probability: null, material: DIAMOND_HORSE_ARMOR," +
                " enchantments: [{type: PROTECTION, level: 2, schedules: [{timezone: Europe/London, days-of-the-week: [MON]}]}]," +
                " durability: 100}",
                equipmentSpecial.toString());
    }

    @Test
    public void testWholeEquipmentSpecialWithoutDefaults() throws Exception {
        HItemEquipmentSpecial equipmentSpecial = getFromConfig(getPreparedConfig(
                "es:",
                "  probability: 50",
                "  material: DIAMOND_HORSE_ARMOR",
                "  durability: 40",
                "  enchantments:",
                "   - e",
                "e:",
                "  type: THORNS",
                "  level: 1",
                "  schedules:",
                "   - s1",
                "s1:",
                "  timezone: Europe/London",
                "  days-of-the-week:",
                "  - MON"),
                getParanoiacCustomLogger(), BODY, "es", "equipment special");
        assertNotNull(equipmentSpecial);
        assertEquals("{probability: 50, material: DIAMOND_HORSE_ARMOR," +
                        " enchantments: [{type: THORNS, level: 1, schedules: [{timezone: Europe/London, days-of-the-week: [MON]}]}]," +
                        " durability: 40}",
                equipmentSpecial.toString());
    }

    @Test
    public void testWholeEquipmentSpecialWithDefaults() throws Exception {
        HItemEquipmentSpecial equipmentSpecial = getFromConfig(getPreparedConfig(
                        "es:",
                        "  material: DIAMOND_HORSE_ARMOR"),
                getCustomLogger(), BODY, "es", "equipment special");
        assertNotNull(equipmentSpecial);
        assertEquals("{probability: null, material: DIAMOND_HORSE_ARMOR," +
                        " enchantments: null, durability: null}",
                equipmentSpecial.toString());
    }

    @Test
    public void testEmptyEquipmentSpecial() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty equipment special");
        getFromConfig(getPreparedConfig(
                "?:"),
                getDebugFearingCustomLogger(), BODY, "es", "equipment special");
    }

    @Test
    public void testEmptyItemInHandValue() throws Exception {
        assertNull(getFromConfig(getPreparedConfig(
                "?:"),
                getCustomLogger(), BODY, "es", "equipment special"));
    }

    @Test
    public void testEmptyProbability() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty probability of equipment special. Use default value 100");
        getFromConfig(getPreparedConfig(
                "es:",
                " k: v"),
                getDebugFearingCustomLogger(), BODY, "es", "equipment special");
    }

    @Test
    public void testEmptyMaterial() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null material of equipment special");
        getFromConfig(getPreparedConfig(
                "es:",
                " k: v"),
                getCustomLogger(), BODY, "es", "equipment special");
    }

    @Test
    public void testEmptyDurability() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty durability of equipment special");
        getFromConfig(getPreparedConfig(
                "es:",
                " probability: 77",
                " material: DIAMOND_HORSE_ARMOR"),
                getDebugFearingCustomLogger(), BODY, "es", "equipment special");
    }

    @Test
    public void testEmptyEnchantments() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty enchantments of equipment special");
        getFromConfig(getPreparedConfig(
                "es:",
                " probability: 77",
                " material: DIAMOND_HORSE_ARMOR",
                " durability: 77"),
                getDebugFearingCustomLogger(), BODY, "es", "equipment special");
    }
}