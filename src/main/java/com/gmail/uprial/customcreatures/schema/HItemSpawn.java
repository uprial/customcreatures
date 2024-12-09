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
    private final EntityType type;
    private final IValue<Integer> amount;

    private HItemSpawn(String title, EntityType type, IValue<Integer> amount) {
        this.title = title;
        this.type = type;
        this.amount = amount;
    }

    public void apply(CustomLogger customLogger, Entity entity) {
        final int n = amount.getValue();
        for(int i = 0; i < n; i++) {
            entity.getWorld().spawnEntity(entity.getLocation(), type);
        }

        if (customLogger.isDebugMode()) {
            customLogger.debug(String.format("Handle %s: %d of %s at %s",
                    title, n, type, format(entity.getLocation().toVector())));
        }
    }

    public static HItemSpawn getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        final EntityType type = getEnum(EntityType.class, config,
                joinPaths(key, "type"), String.format("type of %s", title));

        IValue<Integer> amount = HValue.getIntFromConfig(config, customLogger,
                joinPaths(key, "amount"), String.format("amount of %s", title), 1, 100);
        if (amount == null) {
            amount = new ValueConst<>(1);
        }

        return new HItemSpawn(title, type, amount);
    }

    public String toString() {
        return String.format("{type: %s, amount: %s}", type, amount);
    }
}
