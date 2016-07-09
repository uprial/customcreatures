package com.gmail.uprial.customcreatures.schema;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentLoader {
    public static Class<? extends Enum> get() {
        try {
            Enchantment.class.getField("FROST_WALKER");
            Enchantment.class.getField("MENDING");
            return EnchantmentEnum.class;
        } catch (NoSuchFieldException e) {
            return EnchantmentEnumOld.class;
        }
    }
}
