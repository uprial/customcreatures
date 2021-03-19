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
import static com.gmail.uprial.customcreatures.common.MetadataHelper.*;
import static com.gmail.uprial.customcreatures.common.PersistenceHelper.getPersistentMetadata;
import static com.gmail.uprial.customcreatures.common.PersistenceHelper.setPersistentMetadata;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;
import static org.bukkit.attribute.Attribute.*;

public final class HItemAttributes {
    /*
     Because of rounding of float point variables we need to make sure that
     health of entity is lower than its max. health.
     So, we reduce an entity's health by this value.
      */
    private static double HEALTH_REDUCTION = 0.000001;

    private static final String MK_ATTACK_DAMAGE_MULTIPLIER = "attack-damage-multiplier";

    private static final Map<String, HItemGenericAttribute> KEY_2_GENERIC_ATTRIBUTE
            = ImmutableMap.<String, HItemGenericAttribute>builder()
            .put("base-armor", new HItemGenericAttribute(GENERIC_ARMOR, "base armor", 0.0, MAX_DOUBLE_VALUE))
            .put("follow-range", new HItemGenericAttribute(GENERIC_FOLLOW_RANGE, "follow range", 1.0, 100.0))
            .put("knockback-resistance", new HItemGenericAttribute(GENERIC_KNOCKBACK_RESISTANCE, "knockback resistance", 0.0, 1.0))
            .put("max-health", new HItemGenericAttribute(GENERIC_MAX_HEALTH, "max. health", MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE))
            .put("movement-speed-multiplier", new HItemGenericAttribute(GENERIC_MOVEMENT_SPEED, "movement speed multiplier", MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE))
            .build();

    private static final String MK_INITIAL_MAX_HEALTH = "initial_max_health";
    private static final String MK_INITIAL_FLY_SPEED = "initial_fly_speed";
    private static final String MK_INITIAL_WALK_SPEED = "initial_walk_speed";

    private final String title;
    private final IValue<Double> maxHealthMultiplier;
    private final IValue<Double> attackDamageMultiplier;
    private final Map<String, IValue<Double>> genericAttributes;

    private HItemAttributes(String title, IValue<Double> maxHealthMultiplier,
                            @SuppressWarnings("MethodParameterNamingConvention") IValue<Double> attackDamageMultiplier,
                            Map<String, IValue<Double>> genericAttributes) {
        this.title = title;
        this.maxHealthMultiplier = maxHealthMultiplier;
        this.attackDamageMultiplier = attackDamageMultiplier;
        this.genericAttributes = genericAttributes;
    }

    public void handle(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        // The order makes sense: multiple a max. health after absolute value
        applyGenericAttributes(plugin, customLogger, entity);
        applyMaxHealth(plugin, customLogger, entity);
        applyAttackDamageMultiplier(plugin, customLogger, entity);
    }

    public static Double getAttackDamageMultiplier(CustomCreatures plugin, LivingEntity entity) {
        return getPersistentMetadata(plugin, entity, MK_ATTACK_DAMAGE_MULTIPLIER);
    }

    private void applyGenericAttributes(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        for (Entry<String,IValue<Double>> entry : genericAttributes.entrySet()) {
            String key = entry.getKey();

            Double value = entry.getValue().getValue();

            HItemGenericAttribute genericAttribute = KEY_2_GENERIC_ATTRIBUTE.get(key);
            AttributeInstance attributeInstance = entity.getAttribute(genericAttribute.getAttribute());
            if (attributeInstance == null) {
                customLogger.error(String.format("Can't handle %s of %s of %s: not such attribute",
                        genericAttribute.getTitle(), title, format(entity)));
                continue;
            }

            double baseValue = attributeInstance.getBaseValue();

            switch (key) {
                case "movement-speed-multiplier":
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        applyPlayerMovementSpeedMultiplier(plugin, customLogger, player, value.floatValue());
                        continue;
                    } else {
                        value *= baseValue;
                        attributeInstance.setBaseValue(value);
                    }
                    break;
                default:
                    attributeInstance.setBaseValue(value);
                    break;
            }
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: change %s of %s from %.2f to %.2f",
                        title, genericAttribute.getTitle(), format(entity), baseValue, value));
            }
        }
    }

    private void applyPlayerMovementSpeedMultiplier(CustomCreatures plugin, CustomLogger customLogger, Player player, float multiplier) {
        float oldWalkSpeed = getPlayerInitialValue(plugin, player, MK_INITIAL_WALK_SPEED, player.getWalkSpeed());
        float newWalkSpeed = oldWalkSpeed * multiplier;

        float oldFlySpeed = getPlayerInitialValue(plugin, player, MK_INITIAL_FLY_SPEED, player.getFlySpeed());
        float newFlySpeed = oldFlySpeed * multiplier;
        player.setFlySpeed(newFlySpeed);
        player.setWalkSpeed(newWalkSpeed);

        if (customLogger.isDebugMode()) {
            customLogger.debug(String.format("Handle %s modification: change walk speed of %s from %.2f to %.2f and fly speed from %.2f to %.2f",
                    title, format(player), oldWalkSpeed, newWalkSpeed, oldFlySpeed, newFlySpeed));
        }
    }

    private void applyAttackDamageMultiplier(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        if (attackDamageMultiplier != null) {
            Double newValue = attackDamageMultiplier.getValue();
            setPersistentMetadata(plugin, entity, MK_ATTACK_DAMAGE_MULTIPLIER, newValue);
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: set attack damage multiplier of %s to %.2f",
                        title, format(entity), newValue));
            }
        }
    }

    private void applyMaxHealth(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        if (maxHealthMultiplier != null) {
            AttributeInstance maxHealthAttributeInstance = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            double maxHealth = (entity instanceof Player)
                    /*
                        A player will be summoned many times after each death,
                        so we need to keep its initial attribute in metadata.
                        Other entities neither can have persistent metadata not can be respawned.
                     */
                    ? getPlayerInitialValue(plugin, (Player)entity, MK_INITIAL_MAX_HEALTH, maxHealthAttributeInstance.getBaseValue())
                    : maxHealthAttributeInstance.getBaseValue();
            double newMaxHealth = maxHealth * maxHealthMultiplier.getValue();

            maxHealthAttributeInstance.setBaseValue(newMaxHealth);
            entity.setHealth(newMaxHealth - HEALTH_REDUCTION);
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: change max. health of %s from %.2f to %.2f",
                        title, format(entity), maxHealth, newMaxHealth));
            }
        }
    }

    /*
        The game stores all changes of player properties.
        To avoid cumulative changes after respawn we need to multiply initial values.
      */
    private static <T> T getPlayerInitialValue(CustomCreatures plugin, Player player, String key, T defaultValue) {
        T value = getMetadata(player, key);
        if (value == null) {
            value = defaultValue;
            setMetadata(plugin, player, key, value);
        }

        return value;
    }

    public static HItemAttributes getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        IValue<Double> maxHealthMultiplier = HValue.getDoubleFromConfig(config, customLogger, joinPaths(key, "max-health-multiplier"),
                String.format("max. health multiplier in %s", title), MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE);

        //noinspection LocalVariableNamingConvention
        IValue<Double> attackDamageMultiplier = HValue.getDoubleFromConfig(config, customLogger, joinPaths(key, "attack-damage-multiplier"),
                String.format("attack damage multiplier in %s", title), MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE);

        Map<String, IValue<Double>> genericAttributes = new HashMap<>();
        IValue<Double> item;

        for (Entry<String,HItemGenericAttribute> entry : KEY_2_GENERIC_ATTRIBUTE.entrySet()) {
            HItemGenericAttribute genericAttribute = entry.getValue();
            item = HValue.getDoubleFromConfig(config, customLogger, joinPaths(key, entry.getKey()),
                    String.format("%s in %s", genericAttribute.getTitle(), title),
                    genericAttribute.getHardMin(), genericAttribute.getHardMax());
            if (item != null) {
                genericAttributes.put(entry.getKey(), item);
            }
        }

        if ((maxHealthMultiplier == null) && (attackDamageMultiplier == null) && (genericAttributes.isEmpty())) {
            throw new InvalidConfigException(String.format("No modifications found in %s", title));
        }

        return new HItemAttributes(key, maxHealthMultiplier, attackDamageMultiplier, genericAttributes);
    }

    public String toString() {
        List<String> items = new ArrayList<>();
        items.add(String.format("max-health-multiplier: %s", maxHealthMultiplier));
        items.add(String.format("attack-damage-multiplier: %s", attackDamageMultiplier));

        IValue<Double> item;
        for (String key : KEY_2_GENERIC_ATTRIBUTE.keySet()) {
            item = genericAttributes.containsKey(key) ? genericAttributes.get(key) : null;
            items.add(String.format("%s: %s", key, item));
        }
        return String.format("[%s]", joinStrings(", ", items));
    }
}
