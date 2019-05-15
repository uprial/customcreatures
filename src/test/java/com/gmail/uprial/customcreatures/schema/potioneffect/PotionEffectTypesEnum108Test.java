package com.gmail.uprial.customcreatures.schema.potioneffect;

import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PotionEffectTypesEnum108Test {
    @Test
    public void testSequence() throws Exception {
        int i = 1;
        for(PotionEffectTypesEnum108 potionEffectType : PotionEffectTypesEnum108.values()) {
            //noinspection deprecation
            assertEquals(i, potionEffectType.getType().getId());
            i += 1;
        }
    }

    @Test
    public void testConsistency() throws Exception {
        assertEquals(PotionEffectTypesEnum108.values().length + 9, PotionEffectType.class.getFields().length);
    }
}