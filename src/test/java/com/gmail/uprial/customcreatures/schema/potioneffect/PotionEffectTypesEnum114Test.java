package com.gmail.uprial.customcreatures.schema.potioneffect;

import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PotionEffectTypesEnum114Test {
    @Test
    public void testConsistency() throws Exception {
        assertEquals(PotionEffectTypesEnum.values().length + 0, PotionEffectType.class.getFields().length);
    }
}