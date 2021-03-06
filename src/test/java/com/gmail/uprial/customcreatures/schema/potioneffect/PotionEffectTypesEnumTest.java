package com.gmail.uprial.customcreatures.schema.potioneffect;

import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PotionEffectTypesEnumTest {
    @Test
    public void testSequence() throws Exception {
        int i = 1;
        for(PotionEffectTypesEnum potionEffectType : PotionEffectTypesEnum.values()) {
            //noinspection deprecation
            assertEquals(i, potionEffectType.getType().hashCode());
            i += 1;
        }
    }

    @Test
    public void testConsistency() throws Exception {
        assertEquals(PotionEffectTypesEnum.values().length, PotionEffectType.class.getFields().length);
    }
}