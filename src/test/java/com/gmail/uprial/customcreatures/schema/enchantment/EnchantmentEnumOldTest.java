package com.gmail.uprial.customcreatures.schema.enchantment;

import org.bukkit.enchantments.Enchantment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnchantmentEnumOldTest {
    @Test
    public void testConsistency() throws Exception {
        assertEquals(EnchantmentEnumOld.values().length + 2, Enchantment.class.getFields().length);
    }

}