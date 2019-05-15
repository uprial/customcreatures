package com.gmail.uprial.customcreatures.schema.enchantment;

import org.bukkit.enchantments.Enchantment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnchantmentEnum110Test {
    @Test
    public void testConsistency() throws Exception {
        assertEquals(EnchantmentEnum110.values().length + 10, Enchantment.class.getFields().length);
    }

}