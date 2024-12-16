package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import com.gmail.uprial.customcreatures.schema.enums.PotionEffectTypesEnum;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.common.Utils.seconds2ticks;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getSet;

public final class HItemEffect {
    private final static int INFINITE_DURATION = 0;

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
        for (final PotionEffectTypesEnum effectType : effectTypes) {
            // The 1st level of effect mean 0 in these numbers.
            final int amplifier = strength.getValue() - 1;

            int durationSeconds = duration.getValue();
            if(durationSeconds == INFINITE_DURATION) {
                durationSeconds = PotionEffect.INFINITE_DURATION;
            } else {
                durationSeconds = seconds2ticks(durationSeconds);
            }
            addEffect(customLogger, entity, new PotionEffect(effectType.getType(), durationSeconds, amplifier));
        }
    }

    public static HItemEffect getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        final Set<PotionEffectTypesEnum> effectTypes;
        final IValue<Integer> strength;
        final IValue<Integer> duration;
        //noinspection NestedAssignment
        if ((effectTypes = getSet(PotionEffectTypesEnum.class,
                config, customLogger, joinPaths(key, "types"), String.format("effect types of %s", title))) == null) {
            throw new InvalidConfigException(String.format("Empty effect types of %s", title));
        } else //noinspection NestedAssignment
            if ((strength = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "strength"),
                String.format("strength of %s", title), 1, Integer.MAX_VALUE)) == null) {
            throw new InvalidConfigException(String.format("Empty strength of %s", title));
        } else //noinspection NestedAssignment
                if ((duration = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "duration"),
                String.format("duration of %s", title), 0, Integer.MAX_VALUE)) == null) {
            throw new InvalidConfigException(String.format("Empty duration of %s", title));
        }

        //noinspection unchecked
        return new HItemEffect(title, effectTypes, strength, duration);
    }

    public String toString() {
        return String.format("{types: %s, strength: %s, duration: %s}",
                effectTypes, strength,
                (duration.getValue() == INFINITE_DURATION ? "infinite" : duration));
    }

    private void addEffect(CustomLogger customLogger, LivingEntity entity, PotionEffect effect) {
        //noinspection LocalVariableNamingConvention
        boolean notAffectedOrAffectedMoreWeakly = true;
        for (final PotionEffect currentEffect : entity.getActivePotionEffects()) {
            if(currentEffect.getType() == effect.getType()) {
                if(currentEffect.getAmplifier() > effect.getAmplifier()) {
                    notAffectedOrAffectedMoreWeakly = false;
                } else {
                    entity.removePotionEffect(currentEffect.getType());
                }
            }
        }
        if(notAffectedOrAffectedMoreWeakly) {
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s: add %s to %s",
                        title, format(effect), format(entity)));
            }
            entity.addPotionEffect(effect);
        }
    }
}
