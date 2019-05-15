package com.gmail.uprial.customcreatures.schema.potioneffect;

import org.bukkit.potion.PotionEffectType;

/*
    This module supports backward compatibility of potion effects.
 */
public final class PotionEffectTypesLoader {
    public static Class<? extends Enum> get() {
        try {
            PotionEffectType.class.getField("SLOW_FALLING");
            PotionEffectType.class.getField("CONDUIT_POWER");
            PotionEffectType.class.getField("DOLPHINS_GRACE");
            PotionEffectType.class.getField("BAD_OMEN");
            PotionEffectType.class.getField("HERO_OF_THE_VILLAGE");
            return PotionEffectTypesEnum.class;
        } catch (NoSuchFieldException ignored1) {
            try {
                PotionEffectType.class.getField("GLOWING");
                PotionEffectType.class.getField("LEVITATION");
                PotionEffectType.class.getField("LUCK");
                PotionEffectType.class.getField("UNLUCK");
                return PotionEffectTypesEnum110.class;
            } catch (NoSuchFieldException ignored2) {
                return PotionEffectTypesEnum108.class;
            }
        }
    }
}
