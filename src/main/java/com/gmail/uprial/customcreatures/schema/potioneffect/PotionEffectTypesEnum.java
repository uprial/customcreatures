package com.gmail.uprial.customcreatures.schema.potioneffect;

import org.bukkit.potion.PotionEffectType;

public enum PotionEffectTypesEnum implements IPotionEffectTypesEnum {
    SPEED(PotionEffectType.SPEED),
    SLOWNESS(PotionEffectType.SLOWNESS),
    HASTE(PotionEffectType.HASTE),
    MINING_FATIGUE(PotionEffectType.MINING_FATIGUE),
    STRENGTH(PotionEffectType.STRENGTH),
    INSTANT_HEALTH(PotionEffectType.INSTANT_HEALTH),
    INSTANT_DAMAGE(PotionEffectType.INSTANT_DAMAGE),
    JUMP_BOOST(PotionEffectType.JUMP_BOOST),
    NAUSEA(PotionEffectType.NAUSEA),
    REGENERATION(PotionEffectType.REGENERATION),
    RESISTANCE(PotionEffectType.RESISTANCE),
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
    DARKNESS(PotionEffectType.DARKNESS),
    TRIAL_OMEN(PotionEffectType.TRIAL_OMEN),
    RAID_OMEN(PotionEffectType.RAID_OMEN),
    WIND_CHARGED(PotionEffectType.WIND_CHARGED),
    WEAVING(PotionEffectType.WEAVING),
    OOZING(PotionEffectType.OOZING),
    INFESTED(PotionEffectType.INFESTED);

    private final PotionEffectType type;

    PotionEffectTypesEnum(PotionEffectType type) {
        this.type = type;
    }

    @Override
    public PotionEffectType getType() {
        return type;
    }
}
