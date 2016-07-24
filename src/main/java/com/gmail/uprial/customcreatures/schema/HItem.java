package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import static com.gmail.uprial.customcreatures.common.DoubleHelper.MAX_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.common.DoubleHelper.MIN_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;

public final class HItem {
    private final String name;
    private final HItemFilter filter;
    private final HItemEffectsList effects;
    private final IValue<Double> maxHealth;
    private final HItemEquipment equipment;

    private HItem(String name, HItemFilter filter, HItemEffectsList effects, IValue<Double> maxHealth, HItemEquipment equipment) {
        this.name = name;
        this.filter = filter;
        this.effects = effects;
        this.maxHealth = maxHealth;
        this.equipment = equipment;
    }

    public void handle(CustomLogger customLogger, LivingEntity entity, SpawnReason spawnReason) {
        if (filter.isPassed(entity.getType(), spawnReason)) {
            applyMaxHealth(customLogger, entity);
            applyEffects(customLogger, entity);
            applyEquipment(customLogger, entity);
        }
    }

    private void applyMaxHealth(CustomLogger customLogger, LivingEntity entity) {
        if (maxHealth != null) {
            double value = entity.getMaxHealth() * maxHealth.getValue();
            entity.setMaxHealth(value);
            entity.setHealth(value);
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle '%s' modification: set max. health of %s to %.2f",
                        name, format(entity), value));
            }
        }
    }

    private void applyEffects(CustomLogger customLogger, LivingEntity entity) {
        if (effects != null) {
            effects.apply(customLogger, entity);
        }
    }

    private void applyEquipment(CustomLogger customLogger, LivingEntity entity) {
        if (equipment != null) {
            equipment.apply(customLogger, entity);
        }
    }

    public static HItem getFromConfig(FileConfiguration config, CustomLogger customLogger, String key) throws InvalidConfigException {
        HItemFilter filter = HItemFilter.getFromConfig(config, customLogger, joinPaths(key, "filter"),
                String.format("filter of handler '%s'", key));
        HItemEffectsList effectsList = HItemEffectsList.getFromConfig(config, customLogger, joinPaths(key, "effects"),
                String.format("effects of handler '%s'", key));
        IValue<Double> maxHealth = HValue.getDoubleFromConfig(config, customLogger, joinPaths(key, "max-health"),
                String.format("max. health multiplier of handler '%s'", key), MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE);
        HItemEquipment equipment = HItemEquipment.getFromConfig(config, customLogger, joinPaths(key, "equipment"),
                String.format("equipment of handler '%s'", key));
        if ((maxHealth == null) && (effectsList == null) && (equipment == null)) {
            throw new InvalidConfigException(String.format("No modifications found for handler '%s'", key));
        }

        return new HItem(key, filter, effectsList, maxHealth, equipment);
    }
}
