package com.gmail.uprial.customcreatures.schema.enums;

import com.gmail.uprial.customcreatures.helpers.TestServerBase;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PotionEffectTypesEnumTest extends TestServerBase {
    @Test
    public void testDeprecatedSequence() throws Exception {
        int i = 1;
        for(PotionEffectTypesEnum potionEffectType : PotionEffectTypesEnum.values()) {
            assertEquals(potionEffectType.getType(), PotionEffectType.getById(i));
            i += 1;
        }
    }

    @Test
    public void testConsistency() throws Exception {
        assertEquals(PotionEffectTypesEnum.values().length, PotionEffectType.class.getFields().length);
    }
}