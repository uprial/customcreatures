package com.gmail.uprial.customcreatures.schema.potioneffect;

import org.bukkit.potion.PotionEffectType;

public enum PotionEffectTypesEnum implements IPotionEffectTypesEnum {
    SPEED(PotionEffectType.SPEED),
    SLOW(PotionEffectType.SLOW),
    FAST_DIGGING(PotionEffectType.FAST_DIGGING),
    SLOW_DIGGING(PotionEffectType.SLOW_DIGGING),
    INCREASE_DAMAGE(PotionEffectType.INCREASE_DAMAGE),
    HEAL(PotionEffectType.HEAL),
    HARM(PotionEffectType.HARM),
    JUMP(PotionEffectType.JUMP),
    CONFUSION(PotionEffectType.CONFUSION),
    REGENERATION(PotionEffectType.REGENERATION),
    DAMAGE_RESISTANCE(PotionEffectType.DAMAGE_RESISTANCE),
    FIRE_RESISTANCE(PotionEffectType.FIRE_RESISTANCE),
    WATER_BREATHING(PotionEffectType.WATER_BREATHING),
    INVISIBILITY(PotionEffectType.INVISIBILITY),
    BLINDNESS(PotionEffectType.BLINDNESS),
    NIGHT_VISION(PotionEffectType.NIGHT_VISION),
    HUNGER(PotionEffectType.HUNGER),
    WEAKNESS(PotionEffectType.WEAKNESS),
    POISON(PotionEffectType.POISON),
    WITHER(PotionEffectType.WITHER),
    HEALTH_BOOST(PotionEffectType.HEALTH_BOOST),
    ABSORPTION(PotionEffectType.ABSORPTION),
    SATURATION(PotionEffectType.SATURATION),
    GLOWING(PotionEffectType.GLOWING),
    LEVITATION(PotionEffectType.LEVITATION),
    LUCK(PotionEffectType.LUCK),
    UNLUCK(PotionEffectType.UNLUCK),
    SLOW_FALLING(PotionEffectType.SLOW_FALLING),
    CONDUIT_POWER(PotionEffectType.CONDUIT_POWER),
    DOLPHINS_GRACE(PotionEffectType.DOLPHINS_GRACE),
    BAD_OMEN(PotionEffectType.BAD_OMEN),
    HERO_OF_THE_VILLAGE(PotionEffectType.HERO_OF_THE_VILLAGE),
    DARKNESS(PotionEffectType.DARKNESS);

    private final PotionEffectType type;

    PotionEffectTypesEnum(PotionEffectType type) {
        this.type = type;
    }

    @Override
    public PotionEffectType getType() {
        return type;
    }
}
