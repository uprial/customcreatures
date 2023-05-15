package com.gmail.uprial.customcreatures.schema.enchantment;
/*
    This module supports backward compatibility of enchantments.
 */
public final class EnchantmentLoader {
    public static Class<? extends Enum> get() {
        return EnchantmentEnum.class;
        /*
        try {
            return EnchantmentEnum.class;
        } catch (NoSuchFieldException ignored) {
            return EnchantmentEnum114.class;
        }
        */
    }
}
