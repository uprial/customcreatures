package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

import static com.gmail.uprial.customcreatures.common.DoubleHelper.MAX_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.common.DoubleHelper.MIN_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;

public final class HItem {
    private static final String INITIAL_MAX_HEALTH_METADATA_KEY = "initial_max_health";

    private final String name;
    private final HItemFilter filter;
    private final HItemEffectsList effects;
    private final IValue<Double> maxHealthMultiplier;
    private final HItemEquipment equipment;

    private HItem(String name, HItemFilter filter, HItemEffectsList effects, IValue<Double> maxHealthMultiplier, HItemEquipment equipment) {
        this.name = name;
        this.filter = filter;
        this.effects = effects;
        this.maxHealthMultiplier = maxHealthMultiplier;
        this.equipment = equipment;
    }

    public void handle(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity, SpawnReason spawnReason) {
        if (filter.isPassed(entity.getType(), spawnReason)) {
            applyMaxHealth(plugin, customLogger, entity);
            applyEffects(plugin, customLogger, entity);
            applyEquipment(plugin, customLogger, entity);
        }
    }

    private void applyMaxHealth(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        if (maxHealthMultiplier != null) {
            double maxHealth = getMaxHealth(plugin, entity);
            entity.setMaxHealth(maxHealth);
            entity.setHealth(maxHealth);
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle '%s' modification: set max. health of %s to %.2f",
                        name, format(entity), maxHealth));
            }
        }
    }

    private void applyEffects(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        if (effects != null) {
            effects.apply(plugin, customLogger, entity);
        }
    }

    private void applyEquipment(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        if (equipment != null) {
            equipment.apply(plugin, customLogger, entity);
        }
    }

    private double getMaxHealth(CustomCreatures plugin, LivingEntity entity) {
        double initialMaxHealth = (entity instanceof Player)
                ? getInitialMaxHealth(plugin, entity)
                : entity.getMaxHealth();

        return initialMaxHealth * maxHealthMultiplier.getValue();
    }
    /*
        The game stores all changes of max. health of players.
        To avoid cumulative changes after respawn we need to multiply initial max. health.
      */
    private static double getInitialMaxHealth(CustomCreatures plugin, LivingEntity entity) {
        Double maxHealth = null;
        if (entity.hasMetadata(INITIAL_MAX_HEALTH_METADATA_KEY)) {
            List<MetadataValue> metadataValues = entity.getMetadata(INITIAL_MAX_HEALTH_METADATA_KEY);
            if (!metadataValues.isEmpty()) {
                maxHealth = metadataValues.get(0).asDouble();
            }
        }
        if (maxHealth == null) {
            maxHealth = entity.getMaxHealth();

            MetadataValue metadataValue = new FixedMetadataValue(plugin, maxHealth);
            entity.setMetadata(INITIAL_MAX_HEALTH_METADATA_KEY, metadataValue);
        }

        return maxHealth;
    }

    public static HItem getFromConfig(FileConfiguration config, CustomLogger customLogger, String key) throws InvalidConfigException {
        HItemFilter filter = HItemFilter.getFromConfig(config, customLogger, joinPaths(key, "filter"),
                String.format("filter of handler '%s'", key));
        HItemEffectsList effectsList = HItemEffectsList.getFromConfig(config, customLogger, joinPaths(key, "effects"),
                String.format("effects of handler '%s'", key));
        IValue<Double> maxHealthMultiplier = HValue.getDoubleFromConfig(config, customLogger, joinPaths(key, "max-health"),
                String.format("max. health multiplier of handler '%s'", key), MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE);
        HItemEquipment equipment = HItemEquipment.getFromConfig(config, customLogger, joinPaths(key, "equipment"),
                String.format("equipment of handler '%s'", key));
        if ((maxHealthMultiplier == null) && (effectsList == null) && (equipment == null)) {
            throw new InvalidConfigException(String.format("No modifications found for handler '%s'", key));
        }

        return new HItem(key, filter, effectsList, maxHealthMultiplier, equipment);
    }

    public String toString() {
        return String.format("[name: %s, filter: %s, effects: %s, max-health: %s, equipment: %s]",
                name, filter, effects, maxHealthMultiplier, equipment);
    }
}
