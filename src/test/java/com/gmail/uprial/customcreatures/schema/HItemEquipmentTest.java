package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemEquipment.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HItemEquipmentTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeEquipment() throws Exception {
        HItemEquipment itemEquipment = getFromConfig(getPreparedConfig(
                "e:",
                " helmet:",
                "  material-type: IRON",
                " leggings:",
                "  material-type: GOLD",
                " main-hand:",
                "  material: DIAMOND_SWORD"),
                getParanoiacCustomLogger(), "e", "equipment");
        //noinspection ConstantConditions
        assertEquals("[helmet: [probability: null, material-type: IRON, enchantments: null, drop-chance: 0, durability: null]," +
                " boots: null, chest: null," +
                " leggings: [probability: null, material-type: GOLD, enchantments: null, drop-chance: 0, durability: null]," +
                " main-hand: [probability: null, material: DIAMOND_SWORD, amount: 1, enchantments: null, drop-chance: 0, durability: null]," +
                " off-hand: null]",
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
    public void testNoModifications() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No cloths or tools found in equipment");
        getFromConfig(getPreparedConfig(
                "e:",
                " k: v"),
                getParanoiacCustomLogger(), "e", "equipment");
    }
}