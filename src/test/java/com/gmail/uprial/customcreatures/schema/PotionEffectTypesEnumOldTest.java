package com.gmail.uprial.customcreatures.schema;

import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PotionEffectTypesEnumOldTest {
    @Test
    public void testSequence() throws Exception {
        int i = 1;
        for(PotionEffectTypesEnumOld potionEffectTypesEnumOld : PotionEffectTypesEnumOld.values()) {
            assertEquals(i, potionEffectTypesEnumOld.getType().getId());
            i += 1;
        }
    }

    @Test
    public void testConsistency() throws Exception {
        assertEquals(PotionEffectTypesEnumOld.values().length + 4, PotionEffectType.class.getFields().length);
    }
}