package com.gmail.uprial.customcreatures.schema.potioneffect;

import org.bukkit.potion.PotionEffectType;

/*
    This module supports backward compatibility of potion effects.
 */
public final class PotionEffectTypesLoader {
    public static Class<? extends Enum> get() {
        return PotionEffectTypesEnum.class;
        /*
        try {
            PotionEffectType.class.getField("HERO_OF_THE_VILLAGE");
        } catch (NoSuchFieldException ignored) {
            return PotionEffectTypesEnum114.class;
        }
        */
    }
}
