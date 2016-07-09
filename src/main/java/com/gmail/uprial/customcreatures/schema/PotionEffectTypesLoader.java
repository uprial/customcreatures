package com.gmail.uprial.customcreatures.schema;

import org.bukkit.potion.PotionEffectType;

public class PotionEffectTypesLoader {
    public static Class<? extends Enum> get() {
        try {
            PotionEffectType.class.getField("GLOWING");
            PotionEffectType.class.getField("LEVITATION");
            PotionEffectType.class.getField("LUCK");
            PotionEffectType.class.getField("UNLUCK");
            return PotionEffectTypesEnum.class;
        } catch (NoSuchFieldException e) {
            return PotionEffectTypesEnumOld.class;
        }
    }
}
