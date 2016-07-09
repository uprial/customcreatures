package com.gmail.uprial.customcreatures.schema;

import org.bukkit.enchantments.Enchantment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnchantmentEnumTest {
    @Test
    public void testConsistency() throws Exception {
        assertEquals(EnchantmentEnum.values().length, Enchantment.class.getFields().length);
    }

}