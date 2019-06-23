package com.gmail.uprial.customcreatures.schema.enchantment;

import com.gmail.uprial.customcreatures.schema.potioneffect.PotionEffectTypesEnum;
import org.bukkit.enchantments.Enchantment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnchantmentEnum114Test {
    @Test
    public void testConsistency() throws Exception {
        assertEquals(PotionEffectTypesEnum.values().length + 5, Enchantment.class.getFields().length);
    }

}