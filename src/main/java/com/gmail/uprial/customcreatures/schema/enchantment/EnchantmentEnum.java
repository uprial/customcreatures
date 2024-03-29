package com.gmail.uprial.customcreatures.schema.enchantment;

import org.bukkit.enchantments.Enchantment;

public enum EnchantmentEnum implements IEnchantmentEnum {
    PROTECTION_ENVIRONMENTAL(Enchantment.PROTECTION_ENVIRONMENTAL),
    PROTECTION_FIRE(Enchantment.PROTECTION_FIRE),
    PROTECTION_FALL(Enchantment.PROTECTION_FALL),
    PROTECTION_EXPLOSIONS(Enchantment.PROTECTION_EXPLOSIONS),
    PROTECTION_PROJECTILE(Enchantment.PROTECTION_PROJECTILE),
    OXYGEN(Enchantment.OXYGEN),
    WATER_WORKER(Enchantment.WATER_WORKER),
    THORNS(Enchantment.THORNS),
    DEPTH_STRIDER(Enchantment.DEPTH_STRIDER),
    FROST_WALKER(Enchantment.FROST_WALKER),
    BINDING_CURSE(Enchantment.BINDING_CURSE),
    DAMAGE_ALL(Enchantment.DAMAGE_ALL),
    DAMAGE_UNDEAD(Enchantment.DAMAGE_UNDEAD),
    DAMAGE_ARTHROPODS(Enchantment.DAMAGE_ARTHROPODS),
    KNOCKBACK(Enchantment.KNOCKBACK),
    FIRE_ASPECT(Enchantment.FIRE_ASPECT),
    LOOT_BONUS_MOBS(Enchantment.LOOT_BONUS_MOBS),
    SWEEPING_EDGE(Enchantment.SWEEPING_EDGE),
    DIG_SPEED(Enchantment.DIG_SPEED),
    SILK_TOUCH(Enchantment.SILK_TOUCH),
    DURABILITY(Enchantment.DURABILITY),
    LOOT_BONUS_BLOCKS(Enchantment.LOOT_BONUS_BLOCKS),
    ARROW_DAMAGE(Enchantment.ARROW_DAMAGE),
    ARROW_KNOCKBACK(Enchantment.ARROW_KNOCKBACK),
    ARROW_FIRE(Enchantment.ARROW_FIRE),
    ARROW_INFINITE(Enchantment.ARROW_INFINITE),
    LUCK(Enchantment.LUCK),
    LURE(Enchantment.LURE),
    LOYALTY(Enchantment.LOYALTY),
    IMPALING(Enchantment.IMPALING),
    RIPTIDE(Enchantment.RIPTIDE),
    CHANNELING(Enchantment.CHANNELING),
    MULTISHOT(Enchantment.MULTISHOT),
    QUICK_CHARGE(Enchantment.QUICK_CHARGE),
    PIERCING(Enchantment.PIERCING),
    MENDING(Enchantment.MENDING),
    VANISHING_CURSE(Enchantment.VANISHING_CURSE),
    SOUL_SPEED(Enchantment.SOUL_SPEED),
    SWIFT_SNEAK(Enchantment.SWIFT_SNEAK);

    private final Enchantment type;

    EnchantmentEnum(Enchantment type) {
        this.type = type;
    }

    @Override
    public Enchantment getType() {
        return type;
    }
}
