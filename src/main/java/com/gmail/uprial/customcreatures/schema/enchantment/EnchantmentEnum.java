package com.gmail.uprial.customcreatures.schema.enchantment;

import org.bukkit.enchantments.Enchantment;

public enum EnchantmentEnum implements IEnchantmentEnum {
    PROTECTION(Enchantment.PROTECTION),
    FIRE_PROTECTION(Enchantment.FIRE_PROTECTION),
    FEATHER_FALLING(Enchantment.FEATHER_FALLING),
    BLAST_PROTECTION(Enchantment.BLAST_PROTECTION),
    PROJECTILE_PROTECTION(Enchantment.PROJECTILE_PROTECTION),
    RESPIRATION(Enchantment.RESPIRATION),
    AQUA_AFFINITY(Enchantment.AQUA_AFFINITY),
    THORNS(Enchantment.THORNS),
    DEPTH_STRIDER(Enchantment.DEPTH_STRIDER),
    FROST_WALKER(Enchantment.FROST_WALKER),
    BINDING_CURSE(Enchantment.BINDING_CURSE),
    SHARPNESS(Enchantment.SHARPNESS),
    SMITE(Enchantment.SMITE),
    BANE_OF_ARTHROPODS(Enchantment.BANE_OF_ARTHROPODS),
    KNOCKBACK(Enchantment.KNOCKBACK),
    FIRE_ASPECT(Enchantment.FIRE_ASPECT),
    LOOTING(Enchantment.LOOTING),
    SWEEPING_EDGE(Enchantment.SWEEPING_EDGE),
    EFFICIENCY(Enchantment.EFFICIENCY),
    SILK_TOUCH(Enchantment.SILK_TOUCH),
    UNBREAKING(Enchantment.UNBREAKING),
    FORTUNE(Enchantment.FORTUNE),
    POWER(Enchantment.POWER),
    PUNCH(Enchantment.PUNCH),
    FLAME(Enchantment.FLAME),
    INFINITY(Enchantment.INFINITY),
    LUCK_OF_THE_SEA(Enchantment.LUCK_OF_THE_SEA),
    LURE(Enchantment.LURE),
    LOYALTY(Enchantment.LOYALTY),
    IMPALING(Enchantment.IMPALING),
    RIPTIDE(Enchantment.RIPTIDE),
    CHANNELING(Enchantment.CHANNELING),
    MULTISHOT(Enchantment.MULTISHOT),
    QUICK_CHARGE(Enchantment.QUICK_CHARGE),
    PIERCING(Enchantment.PIERCING),
    DENSITY(Enchantment.DENSITY),
    BREACH(Enchantment.BREACH),
    WIND_BURST(Enchantment.WIND_BURST),
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
