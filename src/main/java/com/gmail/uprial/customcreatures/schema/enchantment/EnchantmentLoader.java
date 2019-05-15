package com.gmail.uprial.customcreatures.schema.enchantment;

import org.bukkit.enchantments.Enchantment;

/*
    This module supports backward compatibility of enchantments.
 */
public final class EnchantmentLoader {
    public static Class<? extends Enum> get() {
        try {
            Enchantment.class.getField("BINDING_CURSE");
            Enchantment.class.getField("SWEEPING_EDGE");
            Enchantment.class.getField("LOYALTY");
            Enchantment.class.getField("IMPALING");
            Enchantment.class.getField("RIPTIDE");
            Enchantment.class.getField("CHANNELING");
            Enchantment.class.getField("MULTISHOT");
            Enchantment.class.getField("QUICK_CHARGE");
            Enchantment.class.getField("PIERCING");
            Enchantment.class.getField("VANISHING_CURSE");
            return EnchantmentEnum.class;
        } catch (NoSuchFieldException ignored1) {
            try {
                Enchantment.class.getField("FROST_WALKER");
                Enchantment.class.getField("MENDING");
                return EnchantmentEnum110.class;
            } catch (NoSuchFieldException ignored2) {
                return EnchantmentEnum108.class;
            }
        }
    }
}
