package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.common.Utils.seconds2ticks;
import static com.gmail.uprial.customcreatures.config.ConfigReader.getSet;

public class HItemEffect {

    private final String title;
    private final Set<PotionEffectTypesEnum> effectTypes;
    private final IValue<Integer> strength;
    private final IValue<Integer> duration;

    private HItemEffect(String title, Set<PotionEffectTypesEnum> effectTypes, IValue<Integer> strength, IValue<Integer> duration) {
        this.title = title;
        this.effectTypes = effectTypes;
        this.strength = strength;
        this.duration = duration;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity) {
        for (PotionEffectTypesEnum effectType : effectTypes) {
            addEffect(customLogger, entity, new PotionEffect(effectType.getType(), seconds2ticks(duration.getValue()), strength.getValue() - 1));
        }
    }

    public static HItemEffect getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (null == config.get(key)) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        Set<PotionEffectTypesEnum> effectTypes;
        IValue<Integer> strength;
        IValue<Integer> duration;
        if (null == (effectTypes = getSet(PotionEffectTypesEnum.class, config, customLogger, joinPaths(key, "types"), "effect types of " + title))) {
            throw new InvalidConfigException(String.format("Empty effect types of %s", title));
        } else if (null == (strength = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "strength"), "strength of " + title))) {
            throw new InvalidConfigException(String.format("Empty strength of %s", title));
        } else if (null == (duration = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "duration"), "duration of " + title))) {
            throw new InvalidConfigException(String.format("Empty duration of %s", title));
        }

        return new HItemEffect(title, effectTypes, strength, duration);
    }

    public String toString() {
        return String.format("[types: %s, strength: %s, duration: %s]",
                effectTypes, strength, duration);
    }

    private void addEffect(CustomLogger customLogger, LivingEntity entity, PotionEffect effect) {
        boolean hasPowered = false;
        for (PotionEffect currentEffect : entity.getActivePotionEffects()) {
            if(currentEffect.getType() == effect.getType()) {
                if(currentEffect.getAmplifier() > effect.getAmplifier())
                    hasPowered = true;
                else
                    entity.removePotionEffect(currentEffect.getType());
            }
        }
        if(!hasPowered) {
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Hadnle %s: add %s[strength: %d, duration: %d] to %s",
                        title, effect.getType().getName(), effect.getAmplifier(), effect.getDuration(),
                        customLogger.entity2string(entity)));
            }
            entity.addPotionEffect(effect);
        }
    }
}
