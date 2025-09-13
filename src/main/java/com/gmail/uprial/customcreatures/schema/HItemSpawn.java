package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import com.gmail.uprial.customcreatures.schema.numerics.ValueConst;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import static com.gmail.uprial.customcreatures.common.DoubleHelper.MAX_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.common.DoubleHelper.formatDoubleValue;
import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getEnum;
import static com.gmail.uprial.customcreatures.config.ConfigReaderNumbers.getDouble;
import static com.gmail.uprial.customcreatures.config.ConfigReaderNumbers.getInt;

public class HItemSpawn {
    private final String title;
    private final Probability probability;
    private final EntityType type;
    private final IValue<Integer> amount;
    private final IValue<Boolean> lightingOnSpawn;
    private final Integer limitAmount;
    private final Double limitRange;

    private HItemSpawn(final String title,
                       final Probability probability,
                       final EntityType type,
                       final IValue<Integer> amount,
                       final IValue<Boolean> lightingOnSpawn,
                       final Integer limitAmount,
                       final Double limitRange) {
        this.title = title;
        this.probability = probability;
        this.type = type;
        this.amount = amount;
        this.lightingOnSpawn = lightingOnSpawn;
        this.limitAmount = limitAmount;
        this.limitRange = limitRange;
    }

    public void apply(CustomLogger customLogger, Entity entity) {
        if ((probability == null) || (probability.isPassed())) {
            final Location spawnLocation = entity.getLocation();
            int spawnAmount = amount.getValue();

            if((limitAmount != null) && (limitRange != null)) {
                int existingAmount = 0;
                for(final Entity friend : entity.getWorld().getEntities()) {
                    if((friend.getType().equals(type))
                            && (friend.isValid())
                            && (friend.getLocation().distance(spawnLocation) < limitRange)) {

                        existingAmount ++;
                        if(limitAmount - existingAmount < 1) {
                            if (customLogger.isDebugMode()) {
                                customLogger.debug(String.format("Handle %s for %s: suppressed %d x %s by limit",
                                        title, format(entity), spawnAmount, type));
                            }
                            return;
                        }
                    }
                }
                if(spawnAmount > limitAmount - existingAmount) {
                    final int suppressedAmount = spawnAmount - (limitAmount - existingAmount);
                    spawnAmount -= suppressedAmount;
                    if (customLogger.isDebugMode()) {
                        customLogger.debug(String.format("Handle %s for %s: suppressed %d x %s by limit",
                                title, format(entity), suppressedAmount, type));
                    }
                }
            }
            for (int i = 0; i < spawnAmount; i++) {
                entity.getWorld().spawnEntity(spawnLocation, type);
            }
            if ((lightingOnSpawn != null) && (lightingOnSpawn.getValue())) {
                entity.getWorld().strikeLightningEffect(spawnLocation);
            }

            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s for %s: spawned %d x %s",
                        title, format(entity), spawnAmount, type));
            }
        }
    }

    public static HItemSpawn getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        final Probability probability = Probability.getFromConfig(config, customLogger, joinPaths(key, "probability"),
                String.format("probability of %s", title));

        final EntityType type = getEnum(EntityType.class, config,
                joinPaths(key, "type"), String.format("type of %s", title));

        IValue<Integer> amount = HValue.getIntFromConfig(config, customLogger,
                joinPaths(key, "amount"), String.format("amount of %s", title), 1, 100);
        if (amount == null) {
            amount = new ValueConst<>(1);
        }

        final IValue<Boolean> lightingOnSpawn = HValue.getBooleanFromConfig(config, customLogger, joinPaths(key, "lighting-on-spawn"),
                String.format("'lighting on spawn' flag in %s", title));

        final String limitKey = joinPaths(key, "limit");
        final String limitTitle = String.format("limit of %s", title);
        final Integer limitAmount;
        final Double limitRange;
        if (config.get(limitKey) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", limitTitle));
            limitAmount = null;
            limitRange = null;
        } else {
            limitAmount = getInt(config, customLogger,
                    joinPaths(limitKey, "amount"), String.format("amount of %s", limitTitle), 1, Integer.MAX_VALUE);
            limitRange = getDouble(config, customLogger,
                    joinPaths(limitKey, "range"), String.format("range of %s", limitTitle), 0.0D, MAX_DOUBLE_VALUE);
        }

        return new HItemSpawn(title, probability, type, amount, lightingOnSpawn, limitAmount, limitRange);
    }

    public String toString() {
        return String.format("{probability: %s, type: %s, amount: %s, lighting-on-spawn: %s, limit: {amount: %d, range: %s}}",
                probability, type, amount, lightingOnSpawn, limitAmount, formatDoubleValue(limitRange));
    }
}
