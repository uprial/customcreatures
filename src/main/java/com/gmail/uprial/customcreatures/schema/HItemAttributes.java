package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import com.google.common.collect.ImmutableMap;
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
import static com.gmail.uprial.customcreatures.common.MetadataHelper.getMetadata;
import static com.gmail.uprial.customcreatures.common.MetadataHelper.setMetadata;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;
import static com.gmail.uprial.customcreatures.config.ConfigUtils.getParentPath;
import static org.bukkit.attribute.Attribute.*;

public final class HItemAttributes {
    private static boolean backwardCompatibility = false;

    private static final String MK_ATTACK_DAMAGE_MULTIPLIER = "attack-damage-multiplier";
    private static final String MK_ORIGINAL_FOLLOW_RANGE = "original-follow-range";

    private static final Map<String, HItemGenericAttribute> KEY_2_GENERIC_ATTRIBUTE
            = ImmutableMap.<String, HItemGenericAttribute>builder()
            .put("base-armor", new HItemGenericAttribute(GENERIC_ARMOR, "base armor"))
            .put("follow-range", new HItemGenericAttribute(GENERIC_FOLLOW_RANGE, "follow range", 1.0, 100.0))
            .put("knockback-resistance", new HItemGenericAttribute(GENERIC_KNOCKBACK_RESISTANCE, "knockback resistance", 0.0, 1.0))
            .put("max-health", new HItemGenericAttribute(GENERIC_MAX_HEALTH, "max. health", MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE))
            .put("movement-speed", new HItemGenericAttribute(GENERIC_MOVEMENT_SPEED, "movement speed"))
            .build();

    private static final String INITIAL_MAX_HEALTH_METADATA_KEY = "initial_max_health";

    private final String title;
    private final IValue<Double> maxHealthMultiplier;
    private final IValue<Double> attackDamageMultiplier;
    private final Map<String, IValue<Double>> genericAttributes;

    private HItemAttributes(String title, IValue<Double> maxHealthMultiplier, IValue<Double> attackDamageMultiplier,
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

    public static Double getAttackDamageMultiplier(LivingEntity entity) {
        return getMetadata(entity, MK_ATTACK_DAMAGE_MULTIPLIER);
    }

    public static Double getOriginalFollowRange(LivingEntity entity) {
        return getMetadata(entity, MK_ORIGINAL_FOLLOW_RANGE);
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
                case "follow-range":
                    setMetadata(plugin, entity, MK_ORIGINAL_FOLLOW_RANGE, baseValue);
                    attributeInstance.setBaseValue(value);
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

    private void applyAttackDamageMultiplier(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        if (attackDamageMultiplier != null) {
            Double value = attackDamageMultiplier.getValue();
            setMetadata(plugin, entity, MK_ATTACK_DAMAGE_MULTIPLIER, value);
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: set attack damage multiplier of %s to %.2f",
                        title, format(entity), value));
            }
        }
    }

    private void applyMaxHealth(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        if (maxHealthMultiplier != null) {
            double maxHealth = getMaxHealth(plugin, entity);
            double newMaxHealth = maxHealth * maxHealthMultiplier.getValue();

            entity.setMaxHealth(newMaxHealth);
            entity.setHealth(newMaxHealth);
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: change max. health of %s from %.2f to %.2f",
                        title, format(entity), maxHealth, newMaxHealth));
            }
        }
    }

    private double getMaxHealth(CustomCreatures plugin, LivingEntity entity) {
        double maxHealth = (entity instanceof Player)
                ? getInitialMaxHealth(plugin, entity)
                : entity.getMaxHealth();

        return maxHealth;
    }

    /*
        The game stores all changes of max. health of players.
        To avoid cumulative changes after respawn we need to multiply initial max. health.
      */
    private static double getInitialMaxHealth(CustomCreatures plugin, LivingEntity entity) {
        Double initialMaxHealth = getMetadata(entity, INITIAL_MAX_HEALTH_METADATA_KEY);
        if (initialMaxHealth == null) {
            initialMaxHealth = entity.getMaxHealth();
            setMetadata(plugin, entity, INITIAL_MAX_HEALTH_METADATA_KEY, initialMaxHealth);
        }

        return initialMaxHealth;
    }

    public static void setBackwardCompatibility(boolean value) {
        backwardCompatibility = value;
    }

    public static HItemAttributes getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        // All logic related to this variable is used only for backward compatibility.
        //noinspection LocalVariableNamingConvention
        String maxHealthMultKey = backwardCompatibility
                ? joinPaths(getParentPath(key), "max-health")
                : "/undefined/";

        if ((config.get(key) == null) && (config.get(maxHealthMultKey) == null)) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        if (config.get(maxHealthMultKey) == null) {
            maxHealthMultKey = joinPaths(key, "max-health-multiplier");
        } else {
            customLogger.warning(String.format("Property '%s.max-health' of deprecated, please use '%s.max-health-multiplier'", key, key));
        }

        IValue<Double> maxHealthMultiplier = HValue.getDoubleFromConfig(config, customLogger, maxHealthMultKey,
                String.format("max. health multiplier in %s", title), MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE);

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
