package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import com.gmail.uprial.customcreatures.schema.numerics.ValueConst;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getEnum;

public class HItemSpawn {
    private final String title;
    private final Probability probability;
    private final EntityType type;
    private final IValue<Integer> amount;
    private final IValue<Boolean> lightingOnSpawn;

    private HItemSpawn(String title, Probability probability, EntityType type,
                       IValue<Integer> amount, IValue<Boolean> lightingOnSpawn) {
        this.title = title;
        this.probability = probability;
        this.type = type;
        this.amount = amount;
        this.lightingOnSpawn = lightingOnSpawn;
    }

    public void apply(CustomLogger customLogger, Entity entity) {
        if ((probability == null) || (probability.isPassed())) {
            final int n = amount.getValue();
            for (int i = 0; i < n; i++) {
                entity.getWorld().spawnEntity(entity.getLocation(), type);
            }
            if ((lightingOnSpawn != null) && (lightingOnSpawn.getValue())) {
                entity.getWorld().strikeLightningEffect(entity.getLocation());
            }

            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s: %d of %s",
                        title, n, format(entity)));
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

        return new HItemSpawn(title, probability, type, amount, lightingOnSpawn);
    }

    public String toString() {
        return String.format("{probability: %s, type: %s, amount: %s, lighting-on-spawn: %s}",
                probability, type, amount, lightingOnSpawn);
    }
}
