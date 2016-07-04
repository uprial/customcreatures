package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;

public class HItem {
    private final String name;
    private final HItemFilter filter;
    private final HItemEffectsList effectsList;
    private final IValue<Double> maxHealth;

    private HItem(String name, HItemFilter filter, HItemEffectsList effectsList, IValue<Double> maxHealth) {
        this.name = name;
        this.filter = filter;
        this.effectsList = effectsList;
        this.maxHealth = maxHealth;
    }

    public void handle(CustomLogger customLogger, LivingEntity entity, CreatureSpawnEvent.SpawnReason spawnReason) {
        if (filter.pass(entity.getType(), spawnReason)) {
            applyMaxHealth(customLogger, entity);
            applyEffectsList(customLogger, entity);
        }
    }

    private void applyMaxHealth(CustomLogger customLogger, LivingEntity entity) {
        if (null != maxHealth) {
            double value = entity.getMaxHealth() * maxHealth.getValue();
            entity.setMaxHealth(value);
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handler %s: set max. health of %s to %.2f",
                        name, customLogger.entity2string(entity), value));
            }
        }
    }

    private void applyEffectsList(CustomLogger customLogger, LivingEntity entity) {
        if (null != effectsList) {
            effectsList.apply(customLogger, entity);
        }
    }

    public static HItem getFromConfig(FileConfiguration config, CustomLogger customLogger, String key) throws InvalidConfigException {
        HItemFilter filter = HItemFilter.getFromConfig(config, customLogger, joinPaths(key, "filter"),
                String.format("filter of handler '%s'", key));
        HItemEffectsList effectsList = HItemEffectsList.getFromConfig(config, customLogger, joinPaths(key, "effects"),
                String.format("effects of handler '%s'", key));
        IValue<Double> maxHealth = HValue.getDoubleFromConfig(config, customLogger, joinPaths(key, "max-health"),
                String.format("max. health multiplier of handler '%s'", key));
        if ((null == maxHealth) && (null == effectsList)) {
            throw new InvalidConfigException(String.format("No modifications found for handler '%s'", key));
        }

        return new HItem(key, filter, effectsList, maxHealth);
    }
}
