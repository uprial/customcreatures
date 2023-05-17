package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.ClothType.BARDING;
import static com.gmail.uprial.customcreatures.schema.ClothType.HELMET;
import static com.gmail.uprial.customcreatures.schema.HItemEquipmentCloth.getFromConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class HItemEquipmentClothTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeEquipmentCloth() throws Exception {
        HItemEquipmentCloth itemEquipmentCloth = getFromConfig(getPreparedConfig(
                "eq:",
                "  probability: 100",
                "  drop-chance: 0.5",
                "  material-type: IRON",
                "  durability: 100",
                "  enchantments:",
                "   - e",
                "  e:",
                "    type: THORNS",
                "    level: 1"),
                getParanoiacCustomLogger(), HELMET, "eq", "equipment cloth");
        assertNotNull(itemEquipmentCloth);
        assertEquals("{probability: null, material-type: IRON, enchantments: [{type: THORNS, level: 1}]," +
                " drop-chance: 0.5, durability: 100}",
                itemEquipmentCloth.toString());
    }

    @Test
    public void testWholeEquipmentClothWithoutDefaults() throws Exception {
        HItemEquipmentCloth itemEquipmentCloth = getFromConfig(getPreparedConfig(
                "eq:",
                "  probability: 77",
                "  drop-chance: 0.5",
                "  durability: 50",
                "  material-type: IRON",
                "  enchantments:",
                "   - e",
                "  e:",
                "    type: THORNS",
                "    level: 1"),
                getDebugFearingCustomLogger(), HELMET, "eq", "equipment cloth");
        assertNotNull(itemEquipmentCloth);
        assertEquals("{probability: 77, material-type: IRON, enchantments: [{type: THORNS, level: 1}]," +
                " drop-chance: 0.5, durability: 50}",
                itemEquipmentCloth.toString());
    }

    @Test
    public void testEmptyEquipmentCloth() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty equipment cloth");
        getFromConfig(getPreparedConfig(
                "?:"),
                getDebugFearingCustomLogger(), HELMET, "eq", "equipment cloth");
    }

    @Test
    public void testEmptyEquipmentClothValue() throws Exception {
        assertNull(getFromConfig(getPreparedConfig(
                "?:"),
                getCustomLogger(), HELMET, "eq", "equipment cloth"));
    }

    @Test
    public void testEmptyProbability() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty probability of equipment cloth. Use default value 100");
        getFromConfig(getPreparedConfig(
                "eq:",
                " k: v"),
                getDebugFearingCustomLogger(), HELMET, "eq", "equipment cloth");
    }

    @Test
    public void testEmptyMaterialType() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null material type of equipment cloth");
        getFromConfig(getPreparedConfig(
                "eq:",
                " k: v"),
                getCustomLogger(), HELMET, "eq", "equipment cloth");
    }

    @Test
    public void testEmptyDropChance() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty drop chance of equipment cloth. Use default value 0.085");
        getFromConfig(getPreparedConfig(
                "eq:",
                " probability: 50",
                " material-type: IRON"),
                getDebugFearingCustomLogger(), HELMET, "eq", "equipment cloth");
    }

    @Test
    public void testEmptyDurability() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty durability of equipment cloth");
        getFromConfig(getPreparedConfig(
                "eq:",
                " probability: 77",
                " drop-chance: 0.77",
                " material-type: IRON"),
                getDebugFearingCustomLogger(), HELMET, "eq", "equipment cloth");
    }

    @Test
    public void testEmptyEnchantments() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty enchantments of equipment cloth");
        getFromConfig(getPreparedConfig(
                "eq:",
                " probability: 77",
                " drop-chance: 0.77",
                " durability: 77",
                " material-type: IRON"),
                getDebugFearingCustomLogger(), HELMET, "eq", "equipment cloth");
    }

    @Test
    public void testNotExistingItem() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid item material type 'LEATHER_BARDING' of equipment cloth");
        getFromConfig(getPreparedConfig(
                "eq:",
                " material-type: LEATHER"),
                getCustomLogger(), BARDING, "eq", "equipment cloth");
    }
}