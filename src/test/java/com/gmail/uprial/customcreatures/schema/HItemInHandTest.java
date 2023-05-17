package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemInHand.getFromConfig;
import static com.gmail.uprial.customcreatures.schema.HandType.MAIN_HAND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class HItemInHandTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeItemInHand() throws Exception {
        HItemInHand itemInHand = getFromConfig(getPreparedConfig(
                "i:",
                "  probability: 100",
                "  drop-chance: 1",
                "  durability: 100",
                "  material: DIAMOND_SWORD",
                "  amount: 1",
                "  enchantments:",
                "   - e1",
                "e1:",
                " type: PROTECTION_ENVIRONMENTAL",
                " level: 2"),
                getParanoiacCustomLogger(), MAIN_HAND, "i", "item in hand");
        assertNotNull(itemInHand);
        assertEquals("{probability: null, material: DIAMOND_SWORD, amount: 1," +
                " enchantments: [{type: PROTECTION_ENVIRONMENTAL, level: 2}]," +
                " drop-chance: 1, durability: 100}",
                itemInHand.toString());
    }

    @Test
    public void testWholeItemInHandWithoutDefaults() throws Exception {
        HItemInHand itemInHand = getFromConfig(getPreparedConfig(
                "i:",
                "  probability: 50",
                "  material: DIAMOND_SWORD",
                "  amount: 10",
                "  enchantments:",
                "   - e",
                "  e:",
                "    type: THORNS",
                "    level: 1",
                "  drop-chance: 0.5",
                "  durability: 40"),
                getParanoiacCustomLogger(), MAIN_HAND, "i", "item in hand");
        assertNotNull(itemInHand);
        assertEquals("{probability: 50, material: DIAMOND_SWORD, amount: 10, " +
                        "enchantments: [{type: THORNS, level: 1}]," +
                        " drop-chance: 0.5, durability: 40}",
                itemInHand.toString());
    }

    @Test
    public void testEmptyItemInHand() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty item in hand");
        getFromConfig(getPreparedConfig(
                "?:"),
                getDebugFearingCustomLogger(), MAIN_HAND, "i", "item in hand");
    }

    @Test
    public void testEmptyItemInHandValue() throws Exception {
        assertNull(getFromConfig(getPreparedConfig(
                "?:"),
                getCustomLogger(), MAIN_HAND, "i", "item in hand"));
    }

    @Test
    public void testEmptyProbability() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty probability of item in hand. Use default value 100");
        getFromConfig(getPreparedConfig(
                "i:",
                " k: v"),
                getDebugFearingCustomLogger(), MAIN_HAND, "i", "item in hand");
    }

    @Test
    public void testEmptyMaterial() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null material of item in hand");
        getFromConfig(getPreparedConfig(
                "i:",
                " k: v"),
                getCustomLogger(), MAIN_HAND, "i", "item in hand");
    }

    @Test
    public void testEmptyAmount() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty amount of item in hand. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "i:",
                " probability: 50",
                " material: DIAMOND_SWORD"),
                getDebugFearingCustomLogger(), MAIN_HAND, "i", "item in hand");
    }

    @Test
    public void testEmptyDropChance() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty drop chance of item in hand. Use default value 0.085");
        getFromConfig(getPreparedConfig(
                "i:",
                " probability: 50",
                " material: DIAMOND_SWORD",
                " amount: 1"),
                getDebugFearingCustomLogger(), MAIN_HAND, "i", "item in hand");
    }

    @Test
    public void testEmptyDurability() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty durability of item in hand");
        getFromConfig(getPreparedConfig(
                "i:",
                " probability: 77",
                " drop-chance: 0.77",
                " material: DIAMOND_SWORD",
                " amount: 1"),
                getDebugFearingCustomLogger(), MAIN_HAND, "i", "item in hand");
    }

    @Test
    public void testEmptyEnchantments() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty enchantments of item in hand");
        getFromConfig(getPreparedConfig(
                "i:",
                " probability: 77",
                " drop-chance: 0.77",
                " durability: 77",
                " material: DIAMOND_SWORD",
                " amount: 1"),
                getDebugFearingCustomLogger(), MAIN_HAND, "i", "item in hand");
    }
}