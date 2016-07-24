package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import com.gmail.uprial.customcreatures.schema.potioneffect.IPotionEffectTypesEnum;
import com.gmail.uprial.customcreatures.schema.potioneffect.PotionEffectTypesLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.common.Utils.seconds2ticks;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getSet;

public final class HItemEffect<T extends Enum & IPotionEffectTypesEnum> {

    private final String title;
    private final Set<T> effectTypes;
    private final IValue<Integer> strength;
    private final IValue<Integer> duration;

    private HItemEffect(String title, Set<T> effectTypes, IValue<Integer> strength, IValue<Integer> duration) {
        this.title = title;
        this.effectTypes = effectTypes;
        this.strength = strength;
        this.duration = duration;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity) {
        for (IPotionEffectTypesEnum effectType : effectTypes) {
            addEffect(customLogger, entity, new PotionEffect(effectType.getType(), seconds2ticks(duration.getValue()), strength.getValue()));
        }
    }

    public static HItemEffect getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        Set<? extends Enum> effectTypes;
        IValue<Integer> strength;
        IValue<Integer> duration;
        //noinspection NestedAssignment
        if ((effectTypes = getSet(PotionEffectTypesLoader.get(),
                config, customLogger, joinPaths(key, "types"), String.format("effect types of %s", title))) == null) {
            throw new InvalidConfigException(String.format("Empty effect types of %s", title));
        } else //noinspection NestedAssignment
            if ((strength = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "strength"),
                String.format("strength of %s", title), 1, Integer.MAX_VALUE)) == null) {
            throw new InvalidConfigException(String.format("Empty strength of %s", title));
        } else //noinspection NestedAssignment
                if ((duration = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "duration"),
                String.format("duration of %s", title), 1, Integer.MAX_VALUE)) == null) {
            throw new InvalidConfigException(String.format("Empty duration of %s", title));
        }

        //noinspection unchecked
        return new HItemEffect(title, effectTypes, strength, duration);
    }

    public String toString() {
        return String.format("[types: %s, strength: %s, duration: %s]",
                effectTypes, strength, duration);
    }

    private void addEffect(CustomLogger customLogger, LivingEntity entity, PotionEffect effect) {
        //noinspection LocalVariableNamingConvention
        boolean notAffectedOrAffectedMoreWeakly = true;
        for (PotionEffect currentEffect : entity.getActivePotionEffects()) {
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
