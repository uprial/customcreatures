package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class HItem {
    private final String name;
    private final HItemFilter filter;
    private final IValue maxHealth;

    private HItem(String name, HItemFilter filter, IValue maxHealth) {
        this.name = name;
        this.filter = filter;
        this.maxHealth = maxHealth;
    }

    public void handle(CustomLogger customLogger, LivingEntity entity, CreatureSpawnEvent.SpawnReason spawnReason) {
        if (null == filter || filter.pass(entity.getType(), spawnReason)) {
            applySpeedMultiplier(customLogger, entity);
        }
    }

    private void applySpeedMultiplier(CustomLogger customLogger, LivingEntity entity) {
        if (null != maxHealth) {
            double value = entity.getMaxHealth() * maxHealth.getValue();
            entity.setMaxHealth(value);
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handler %s: set max. health of %s to %.2f",
                        name, customLogger.entity2string(entity), value));
            }
        }
    }

    public static HItem getFromConfig(FileConfiguration config, CustomLogger customLogger, String key) throws InvalidConfigException {
        HItemFilter filter = HItemFilter.getFromConfig(config, customLogger, key + ".filter", "filter of handler", key);
        IValue maxHealth = HValue.getFromConfig(config, customLogger, key + ".max-health", "max. health multiplier of handler", key);
        if (null == maxHealth) {
            throw new InvalidConfigException(String.format("No modifications found for handler '%s'", key));
        }

        return new HItem(key, filter, maxHealth);
    }
}
