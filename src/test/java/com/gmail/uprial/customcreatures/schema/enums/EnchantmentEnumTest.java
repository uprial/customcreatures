package com.gmail.uprial.customcreatures.schema.enums;

import com.gmail.uprial.customcreatures.helpers.TestServerBase;
import org.bukkit.enchantments.Enchantment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnchantmentEnumTest extends TestServerBase {
    @Test
    public void testConsistency() throws Exception {
        assertEquals(EnchantmentEnum.values().length, Enchantment.class.getFields().length);
    }
}