package com.gmail.uprial.customcreatures.schema;

import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PotionEffectTypesEnumTest {
    @Test
    public void testConsistency() throws Exception {
        assertEquals(PotionEffectTypesEnum.values().length + 4, PotionEffectType.class.getFields().length);
    }
}