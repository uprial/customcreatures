package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.BodyType.BARDING;
import static com.gmail.uprial.customcreatures.schema.BodyType.HELMET;
import static com.gmail.uprial.customcreatures.schema.HItemEquipmentCloth.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HItemEquipmentClothTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeEquipmentCloth() throws Exception {
        HItemEquipmentCloth itemEquipmentCloth = getFromConfig(getPreparedConfig(
                "eq:",
                "  material: IRON",
                "  enchantments:",
                "   - e",
                "  e:",
                "    type: THORNS",
                "    level: 1"),
                getParanoiacCustomLogger(), HELMET, "eq", "equipment cloth");
        //noinspection ConstantConditions
        assertEquals("[probability: null, material: IRON, enchantments: {[type: THORNS, level: 1]}, drop-chance: 0]",
                itemEquipmentCloth.toString());
    }

    @Test
    public void testWholeEquipmentClothWithoutDefaults() throws Exception {
        HItemEquipmentCloth itemEquipmentCloth = getFromConfig(getPreparedConfig(
                "eq:",
                "  probability: 77",
                "  drop-chance: 50",
                "  material: IRON",
                "  enchantments:",
                "   - e",
                "  e:",
                "    type: THORNS",
                "    level: 1"),
                getParanoiacCustomLogger(), HELMET, "eq", "equipment cloth");
        //noinspection ConstantConditions
        assertEquals("[probability: 77, material: IRON, enchantments: {[type: THORNS, level: 1]}, drop-chance: 50]",
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
    public void testEmptyProbability() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty probability of equipment cloth. Use default value 100");
        getFromConfig(getPreparedConfig(
                "eq:",
                " k: v"),
                getDebugFearingCustomLogger(), HELMET, "eq", "equipment cloth");
    }

    @Test
    public void testEmptyMaterial() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null or empty material of equipment cloth");
        getFromConfig(getPreparedConfig(
                "eq:",
                " k: v"),
                getParanoiacCustomLogger(), HELMET, "eq", "equipment cloth");
    }

    @Test
    public void testEmptyDropChance() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty drop chance of equipment cloth. Use default value 0");
        getFromConfig(getPreparedConfig(
                "eq:",
                " probability: 50",
                " material: IRON"),
                getDebugFearingCustomLogger(), HELMET, "eq", "equipment cloth");
    }

    @Test
    public void testEmptyEnchantments() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty enchantments of equipment cloth");
        getFromConfig(getPreparedConfig(
                "eq:",
                " probability: 77",
                " drop-chance: 77",
                " material: IRON"),
                getDebugFearingCustomLogger(), HELMET, "eq", "equipment cloth");
    }

    @Test
    public void testNotExistingItem() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid item material 'LEATHER_BARDING' of equipment cloth");
        getFromConfig(getPreparedConfig(
                "eq:",
                " material: LEATHER"),
                getParanoiacCustomLogger(), BARDING, "eq", "equipment cloth");
    }
}