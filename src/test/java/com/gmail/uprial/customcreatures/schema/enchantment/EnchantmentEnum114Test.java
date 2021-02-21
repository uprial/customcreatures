package com.gmail.uprial.customcreatures.schema.enchantment;

import org.bukkit.enchantments.Enchantment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnchantmentEnum114Test {
    @Test
    public void testConsistency() throws Exception {
        assertEquals(EnchantmentEnum.values().length + 0, Enchantment.class.getFields().length);
    }

}