package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import com.google.common.collect.ImmutableMap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.gmail.uprial.customcreatures.common.DoubleHelper.MAX_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.common.DoubleHelper.MIN_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.PersistenceHelper.*;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;
import static org.bukkit.attribute.Attribute.*;

public final class HItemAttributes {
    /*
     Because of rounding of float point variables we need to make sure that
     health of entity is lower than its max. health.
     So, we reduce an entity's health by this value.
      */
    private static final double HEALTH_REDUCTION = 0.000001;

    private static final Map<String, HItemGenericAttribute> KEY_2_GENERIC_ATTRIBUTE
            = ImmutableMap.<String, HItemGenericAttribute>builder()
            .put("base-armor", new HItemGenericAttribute(ARMOR, "base armor", 0.0D, MAX_DOUBLE_VALUE))
            .put("follow-range", new HItemGenericAttribute(FOLLOW_RANGE, "follow range", 1.0D, 100.0D))
            .put("knockback-resistance", new HItemGenericAttribute(KNOCKBACK_RESISTANCE, "knockback resistance", 0.0D, 1.0D))
            .put("max-health", new HItemGenericAttribute(MAX_HEALTH, "max. health", MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE))
            /*
                Even Ghast doesn't have the GENERIC_FLYING_SPEED property,
                so only the GENERIC_MOVEMENT_SPEED property is managed.
             */
            .put("movement-speed-multiplier", new HItemGenericAttribute(MOVEMENT_SPEED, "movement speed multiplier", MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE))
            .put("movement-speed", new HItemGenericAttribute(MOVEMENT_SPEED, "movement speed", MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE))
            .put("scale", new HItemGenericAttribute(SCALE, "scale", MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE))
            .build();

    private static final String MK_MAX_HEALTH_MULTIPLIER_PREFIX = "max-health-multiplier-";
    private static final String MK_MOVEMENT_SPEED_MULTIPLIER_PREFIX = "movement-speed-multiplier-";

    private final String title;
    private final IValue<Double> maxHealthMultiplier;
    private final Map<String, IValue<Double>> genericAttributes;
    private final IValue<Boolean> removeWhenFarAway;

    private HItemAttributes(String title, IValue<Double> maxHealthMultiplier,
                            Map<String, IValue<Double>> genericAttributes,
                            IValue<Boolean> removeWhenFarAway) {
        this.title = title;
        this.maxHealthMultiplier = maxHealthMultiplier;
        this.genericAttributes = genericAttributes;
        this.removeWhenFarAway = removeWhenFarAway;
    }

    public void apply(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity, String handleName) {
        // The order makes sense: multiple a max. health after absolute value
        applyGenericAttributes(plugin, customLogger, entity, handleName);
        applyMaxHealthMultiplier(plugin, customLogger, entity, handleName);
        applyRemoveWhenFarAway(customLogger, entity);
    }

    private void applyGenericAttributes(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity, String handleName) {
        for (Entry<String,IValue<Double>> entry : genericAttributes.entrySet()) {
            final String key = entry.getKey();

            Double value = entry.getValue().getValue();

            final HItemGenericAttribute genericAttribute = KEY_2_GENERIC_ATTRIBUTE.get(key);
            final AttributeInstance attributeInstance = entity.getAttribute(genericAttribute.getAttribute());
            if (attributeInstance == null) {
                customLogger.error(String.format("Can't handle %s of %s of %s: not such attribute",
                        genericAttribute.getTitle(), title, format(entity)));
                continue;
            }

            final double baseValue = attributeInstance.getBaseValue();

            switch (key) {
                case "movement-speed-multiplier":
                    if (entity instanceof Player) {
                        final Player player = (Player) entity;
                        applyPlayerMovementSpeedMultiplier(plugin, customLogger, player, handleName, value.floatValue());
                    } else {
                        value *= baseValue;
                        attributeInstance.setBaseValue(value);
                    }
                    if (customLogger.isDebugMode()) {
                        customLogger.debug(String.format("Handle %s modification: apply %s, change movement speed of %s from %.2f to %.2f",
                                title, genericAttribute.getTitle(), format(entity), baseValue, value));
                    }
                    break;
                default:
                    // Other values are absolute but not multipliers.
                    attributeInstance.setBaseValue(value);
                    if (customLogger.isDebugMode()) {
                        customLogger.debug(String.format("Handle %s modification: change %s of %s from %.2f to %.2f",
                                title, genericAttribute.getTitle(), format(entity), baseValue, value));
                    }
                    break;
            }
        }
    }

    private void applyPlayerMovementSpeedMultiplier(CustomCreatures plugin, CustomLogger customLogger, Player player, String handleName, double newMovementSpeedMultiplier) {
        final double oldMovementSpeedMultiplier = getOldAndReplacePlayerMetadataValue(
                plugin,
                player,
                MK_MOVEMENT_SPEED_MULTIPLIER_PREFIX + handleName,
                1.0D,
                newMovementSpeedMultiplier);

        if(player.isFlying()) {
            final double oldFlySpeed = player.getFlySpeed();
            final double newFlySpeed = oldFlySpeed / oldMovementSpeedMultiplier * newMovementSpeedMultiplier;

            player.setFlySpeed((float)oldFlySpeed);
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: change fly speed of %s from %.2f to %.2f",
                        title, format(player), oldFlySpeed, newFlySpeed));
            }
        } else {
            final double oldWalkSpeed = player.getWalkSpeed();
            final double newWalkSpeed = oldWalkSpeed / oldMovementSpeedMultiplier * newMovementSpeedMultiplier;

            player.setWalkSpeed((float)newWalkSpeed);
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: change walk speed of %s from %.2f to %.2f",
                        title, format(player), oldWalkSpeed, newWalkSpeed));
            }
        }
    }

    private void applyMaxHealthMultiplier(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity, String handleName) {
        if (maxHealthMultiplier != null) {
            final AttributeInstance maxHealthAttributeInstance = entity.getAttribute(Attribute.MAX_HEALTH);
            final double oldMaxHealth = maxHealthAttributeInstance.getBaseValue();
            double newMaxHealth;
            if(entity instanceof Player) {
                /*
                    A player will be summoned many times after each death,
                    so we need to keep its initial attribute in metadata.
                    Other entities neither can have persistent metadata not can be respawned.
                 */
                newMaxHealth = oldMaxHealth / getOldAndReplacePlayerMetadataValue(
                        plugin,
                        (Player)entity,
                        MK_MAX_HEALTH_MULTIPLIER_PREFIX + handleName,
                        1.0,
                        maxHealthMultiplier.getValue()) * maxHealthMultiplier.getValue();
            } else {
                newMaxHealth = oldMaxHealth * maxHealthMultiplier.getValue();
            }

            maxHealthAttributeInstance.setBaseValue(newMaxHealth);
            entity.setHealth(newMaxHealth - HEALTH_REDUCTION);
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: change max. health of %s from %.2f to %.2f",
                        title, format(entity), oldMaxHealth, newMaxHealth));
            }
        }
    }

    private void applyRemoveWhenFarAway(CustomLogger customLogger, LivingEntity entity) {
        if (removeWhenFarAway != null) {
            final Boolean oldValue = entity.getRemoveWhenFarAway();
            final Boolean newValue = removeWhenFarAway.getValue();
            entity.setRemoveWhenFarAway(newValue);
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: set 'remove when far away' flag of %s from %b to %b",
                        title, format(entity), oldValue, newValue));
            }
        }
    }

    /*
        The game stores all changes of player properties.
        To avoid cumulative changes after respawn we need to multiply initial values.
      */
    private static Double getOldAndReplacePlayerMetadataValue(CustomCreatures plugin, Player player, String key, Double defaultValue, Double newValue) {
        Double oldValue = getDoublePersistentMetadata(plugin, player, key);
        if (oldValue == null) {
            oldValue = defaultValue;
        }
        setDoublePersistentMetadata(plugin, player, key, newValue);

        return oldValue;
    }

    public static HItemAttributes getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        final IValue<Double> maxHealthMultiplier = HValue.getDoubleFromConfig(config, customLogger, joinPaths(key, "max-health-multiplier"),
                String.format("max. health multiplier in %s", title), MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE);

        final Map<String, IValue<Double>> genericAttributes = new HashMap<>();
        IValue<Double> item;

        for (Entry<String,HItemGenericAttribute> entry : KEY_2_GENERIC_ATTRIBUTE.entrySet()) {
            final HItemGenericAttribute genericAttribute = entry.getValue();
            item = HValue.getDoubleFromConfig(config, customLogger, joinPaths(key, entry.getKey()),
                    String.format("%s in %s", genericAttribute.getTitle(), title),
                    genericAttribute.getHardMin(), genericAttribute.getHardMax());
            if (item != null) {
                genericAttributes.put(entry.getKey(), item);
            }
        }

        final IValue<Boolean> removeWhenFarAway = HValue.getBooleanFromConfig(config, customLogger, joinPaths(key, "remove-when-far-away"),
                String.format("'remove when far away' flag in %s", title));

        if ((maxHealthMultiplier == null)
                && (genericAttributes.isEmpty())
                && (removeWhenFarAway == null)) {
            throw new InvalidConfigException(String.format("No modifications found in %s", title));
        }

        return new HItemAttributes(key,
                maxHealthMultiplier,
                genericAttributes,
                removeWhenFarAway);
    }

    public String toString() {
        final List<String> items = new ArrayList<>();
        items.add(String.format("max-health-multiplier: %s", maxHealthMultiplier));

        IValue<Double> item;
        for (String key : KEY_2_GENERIC_ATTRIBUTE.keySet()) {
            item = genericAttributes.containsKey(key) ? genericAttributes.get(key) : null;
            items.add(String.format("%s: %s", key, item));
        }

        items.add(String.format("remove-when-far-away: %s", removeWhenFarAway));

        // Though a list is being joined, it's a set of attributes
        return String.format("{%s}", joinStrings(", ", items));
    }
}
