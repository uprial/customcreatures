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

    private final String handlerName;
    private final Set<PotionEffectTypesEnum> effectTypes;
    private final IValue<Integer> strength;
    private final IValue<Integer> duration;

    private HItemEffect(String handlerName, Set<PotionEffectTypesEnum> effectTypes, IValue<Integer> strength, IValue<Integer> duration) {
        this.handlerName = handlerName;
        this.effectTypes = effectTypes;
        this.strength = strength;
        this.duration = duration;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity) {
        for (PotionEffectTypesEnum effectType : effectTypes) {
            addEffect(customLogger, entity, new PotionEffect(effectType.getType(), seconds2ticks(duration.getValue()), strength.getValue() - 1));
        }
    }

    public static HItemEffect getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title, String handlerName) throws InvalidConfigException {
        if (null == config.get(key)) {
            throw new InvalidConfigException(String.format("Empty %s '%s'", title, handlerName));
        }

        Set<PotionEffectTypesEnum> effectTypes;
        IValue<Integer> strength;
        IValue<Integer> duration;
        if (null == (effectTypes = getSet(PotionEffectTypesEnum.class, config, customLogger, joinPaths(key, ".types"), "effect types of " + title, handlerName))) {
            throw new InvalidConfigException(String.format("Empty effect types of %s '%s'", title, handlerName));
        } else if (null == (strength = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "strength"), "strength of handler", key))) {
            throw new InvalidConfigException(String.format("Empty strength of %s '%s'", title, handlerName));
        } else if (null == (duration = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "duration"), "duration of handler", key))) {
            throw new InvalidConfigException(String.format("Empty duration of %s '%s'", title, handlerName));
        }

        return new HItemEffect(handlerName, effectTypes, strength, duration);
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
                customLogger.debug(String.format("Handler %s: add effect %s to %s",
                        handlerName, toString(),customLogger.entity2string(entity)));
            }
            entity.addPotionEffect(effect);
        }
    }
}
