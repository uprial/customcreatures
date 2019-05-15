package com.gmail.uprial.customcreatures.schema.enchantment;

import org.bukkit.enchantments.Enchantment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnchantmentEnum108Test {
    @Test
    public void testConsistency() throws Exception {
        assertEquals(EnchantmentEnum108.values().length + 12, Enchantment.class.getFields().length);
    }

}