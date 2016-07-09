package com.gmail.uprial.customcreatures.schema;

import org.bukkit.enchantments.Enchantment;

public enum EnchantmentEnumOld implements IEnchantmentEnum {
    PROTECTION_ENVIRONMENTAL(Enchantment.PROTECTION_ENVIRONMENTAL),
    PROTECTION_FIRE(Enchantment.PROTECTION_FIRE),
    PROTECTION_FALL(Enchantment.PROTECTION_FALL),
    PROTECTION_EXPLOSIONS(Enchantment.PROTECTION_EXPLOSIONS),
    PROTECTION_PROJECTILE(Enchantment.PROTECTION_PROJECTILE),
    OXYGEN(Enchantment.OXYGEN),
    WATER_WORKER(Enchantment.WATER_WORKER),
    THORNS(Enchantment.THORNS),
    DEPTH_STRIDER(Enchantment.DEPTH_STRIDER),
    DAMAGE_ALL(Enchantment.DAMAGE_ALL),
    DAMAGE_UNDEAD(Enchantment.DAMAGE_UNDEAD),
    DAMAGE_ARTHROPODS(Enchantment.DAMAGE_ARTHROPODS),
    KNOCKBACK(Enchantment.KNOCKBACK),
    FIRE_ASPECT(Enchantment.FIRE_ASPECT),
    LOOT_BONUS_MOBS(Enchantment.LOOT_BONUS_MOBS),
    DIG_SPEED(Enchantment.DIG_SPEED),
    SILK_TOUCH(Enchantment.SILK_TOUCH),
    DURABILITY(Enchantment.DURABILITY),
    LOOT_BONUS_BLOCKS(Enchantment.LOOT_BONUS_BLOCKS),
    ARROW_DAMAGE(Enchantment.ARROW_DAMAGE),
    ARROW_KNOCKBACK(Enchantment.ARROW_KNOCKBACK),
    ARROW_FIRE(Enchantment.ARROW_FIRE),
    ARROW_INFINITE(Enchantment.ARROW_INFINITE),
    LUCK(Enchantment.LUCK),
    LURE(Enchantment.LURE);

    private final Enchantment type;

    EnchantmentEnumOld(Enchantment type) {
        this.type = type;
    }

    public Enchantment getType() {
        return this.type;
    }
}
