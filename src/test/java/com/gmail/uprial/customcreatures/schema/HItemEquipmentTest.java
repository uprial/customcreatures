package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;
import static com.gmail.uprial.customcreatures.schema.ClothType.BARDING;
import static com.gmail.uprial.customcreatures.schema.HItemEquipment.getFromConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class HItemEquipmentTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeEquipment() throws Exception {
        HItemEquipment itemEquipment = getFromConfig(getPreparedConfig(
                "e:",
                " helmet:",
                "  probability: 100",
                "  drop-chance: 1",
                "  durability: 100",
                "  enchantments:",
                "   - e1",
                "  material-type: IRON",
                " boots:",
                "  probability: 100",
                "  drop-chance: 1",
                "  durability: 100",
                "  enchantments:",
                "   - e1",
                "  material-type: GOLDEN",
                " chest:",
                "  probability: 100",
                "  drop-chance: 1",
                "  durability: 100",
                "  enchantments:",
                "   - e1",
                "  material-type: GOLDEN",
                " leggings:",
                "  probability: 100",
                "  drop-chance: 1",
                "  durability: 100",
                "  enchantments:",
                "   - e1",
                "  material-type: GOLDEN",
                " main-hand:",
                "  probability: 100",
                "  drop-chance: 1",
                "  durability: 100",
                "  enchantments:",
                "   - e1",
                "  material: DIAMOND_SWORD",
                "  amount: 1",
                " off-hand:",
                "  probability: 100",
                "  drop-chance: 1",
                "  durability: 100",
                "  enchantments:",
                "   - e1",
                "  material: DIAMOND_SWORD",
                "  amount: 1",
                "e1:",
                " type: PROTECTION_ENVIRONMENTAL",
                " level: 1"),
                getParanoiacCustomLogger(), "e", "equipment");
        assertNotNull(itemEquipment);
        assertEquals("{helmet: {probability: null, material-type: IRON," +
                " enchantments: [{type: PROTECTION_ENVIRONMENTAL, level: 1}]," +
                " drop-chance: 1, durability: 100}," +
                " boots: {probability: null, material-type: GOLDEN," +
                " enchantments: [{type: PROTECTION_ENVIRONMENTAL, level: 1}]," +
                " drop-chance: 1, durability: 100}," +
                " chest: {probability: null, material-type: GOLDEN," +
                " enchantments: [{type: PROTECTION_ENVIRONMENTAL, level: 1}]," +
                " drop-chance: 1, durability: 100}," +
                " leggings: {probability: null, material-type: GOLDEN," +
                " enchantments: [{type: PROTECTION_ENVIRONMENTAL, level: 1}]," +
                " drop-chance: 1, durability: 100}," +
                " main-hand: {probability: null, material: DIAMOND_SWORD, amount: 1," +
                " enchantments: [{type: PROTECTION_ENVIRONMENTAL, level: 1}]," +
                " drop-chance: 1, durability: 100}," +
                " off-hand: {probability: null, material: DIAMOND_SWORD, amount: 1," +
                " enchantments: [{type: PROTECTION_ENVIRONMENTAL, level: 1}]," +
                " drop-chance: 1, durability: 100}}",
                itemEquipment.toString());
    }

    @Test
    public void testEmptyEquipment() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty equipment");
        getFromConfig(getPreparedConfig(
                "?:"),
                getDebugFearingCustomLogger(), "e", "equipment");
    }

    @Test
    public void testEmptyEquipmentValue() throws Exception {
        assertNull(getFromConfig(getPreparedConfig(
                "?:"),
                getCustomLogger(), "e", "equipment"));
    }

    @Test
    public void testNoModifications() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No cloths or tools found in equipment");
        getFromConfig(getPreparedConfig(
                "e:",
                " k: v"),
                getCustomLogger(), "e", "equipment");
    }

    @Test
    public void testAllClothsAndMaterials() throws Exception {
        List<String> config = new ArrayList<>();
        config.add("e:");
        for (final String cloth : new String[]{"helmet", "boots", "chest", "leggings"}) {
            for (final String material : new String[]{"leather", "chainmail", "iron", "diamond", "golden", "netherite"}) {
                config.add(String.format(" %s:", cloth));
                config.add(String.format("  material-type: %s", material));
            }
        }

        HItemEquipment itemEquipment = getFromConfig(getPreparedConfig(joinStrings("\n", config)),
                getCustomLogger(), "e", "equipment");
    }
}