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
                "  durability: 100",
                "  material: DIAMOND_SWORD",
                "  amount: 1",
                "  enchantments:",
                "   - e1",
                "e1:",
                " type: PROTECTION_ENVIRONMENTAL",
                " level: 2"),
                getParanoiacCustomLogger(), "d", "item drop");
        assertNotNull(itemDrop);
        assertEquals("[probability: null, material: DIAMOND_SWORD, amount: 1," +
                " enchantments: {[type: PROTECTION_ENVIRONMENTAL, level: 2]}," +
                " durability: 100]",
                itemDrop.toString());
    }

    @Test
    public void testWholeItemDropWithoutDefaults() throws Exception {
        HItemDrop itemDrop = getFromConfig(getPreparedConfig(
                "d:",
                "  probability: 50",
                "  material: DIAMOND_SWORD",
                "  amount: 10",
                "  enchantments:",
                "   - e",
                "  e:",
                "    type: THORNS",
                "    level: 1",
                "  durability: 40"),
                getParanoiacCustomLogger(), "d", "item drop");
        assertNotNull(itemDrop);
        assertEquals("[probability: 50, material: DIAMOND_SWORD, amount: 10," +
                        " enchantments: {[type: THORNS, level: 1]}," +
                        " durability: 40]",
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
    public void testEmptyAmount() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty amount of item drop. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "d:",
                " probability: 50",
                " material: DIAMOND_SWORD"),
                getDebugFearingCustomLogger(), "d", "item drop");
    }

    @Test
    public void testEmptyDurability() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty durability of item drop");
        getFromConfig(getPreparedConfig(
                "d:",
                " probability: 77",
                " material: DIAMOND_SWORD",
                " amount: 1"),
                getDebugFearingCustomLogger(), "d", "item drop");
    }

    @Test
    public void testEmptyEnchantments() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty enchantments of item drop");
        getFromConfig(getPreparedConfig(
                "d:",
                " probability: 77",
                " durability: 77",
                " material: DIAMOND_SWORD",
                " amount: 1"),
                getDebugFearingCustomLogger(), "d", "item drop");
    }
}