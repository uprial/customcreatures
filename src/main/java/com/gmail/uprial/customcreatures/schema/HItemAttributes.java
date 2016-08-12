package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

import static com.gmail.uprial.customcreatures.common.DoubleHelper.MAX_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.common.DoubleHelper.MIN_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigUtils.getParentPath;

public class HItemAttributes {
    public static boolean saveBackwardCompatibility = true;

    private static final String INITIAL_MAX_HEALTH_METADATA_KEY = "initial_max_health";

    private final String title;
    private final IValue<Double> maxHealthMultiplier;

    private HItemAttributes(String title, IValue<Double> maxHealthMultiplier) {
        this.title = title;
        this.maxHealthMultiplier = maxHealthMultiplier;
    }

    public void handle(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        applyMaxHealth(plugin, customLogger, entity);
    }

    private void applyMaxHealth(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        if (maxHealthMultiplier != null) {
            double maxHealth = getMaxHealth(plugin, entity);
            entity.setMaxHealth(maxHealth);
            entity.setHealth(maxHealth);
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle '%s' modification: set max. health of %s to %.2f",
                        title, format(entity), maxHealth));
            }
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

    public static HItemAttributes getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        // All logic related to this variable is used only for backward compatibility.
        String maxHealthMultiplierKey = saveBackwardCompatibility ? joinPaths(getParentPath(key), "max-health") : "/undefined/";

        if ((config.get(key) == null) && (config.get(maxHealthMultiplierKey) == null)) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        if (config.get(maxHealthMultiplierKey) == null) {
            maxHealthMultiplierKey = joinPaths(key, "max-health-multiplier");
        }
        IValue<Double> maxHealthMultiplier = HValue.getDoubleFromConfig(config, customLogger, maxHealthMultiplierKey,
                String.format("max. health multiplier of %s", title), MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE);

        if ((maxHealthMultiplier == null)) {
            throw new InvalidConfigException(String.format("No modifications found in %s", title));
        }

        return new HItemAttributes(key, maxHealthMultiplier);
    }

    public String toString() {
        return String.format("[max-health-multiplier: %s]",
                maxHealthMultiplier);
    }
}
