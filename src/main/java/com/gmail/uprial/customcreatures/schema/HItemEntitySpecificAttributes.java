package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;

public final class HItemEntitySpecificAttributes {

    private final String title;
    private final ICustomEntity customEntity;

    private HItemEntitySpecificAttributes(String title, ICustomEntity customEntity) {
        this.title = title;
        this.customEntity = customEntity;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity) {
        if (!customEntity.getPossibleEntityTypes().contains(entity.getType())) {
            customLogger.error(String.format("Can't handle %s of %s: not a possible entity type", title, format(entity)));
            return;
        }
        customEntity.apply(customLogger, entity);
    }

    public static HItemEntitySpecificAttributes getFromConfig(FileConfiguration config, CustomLogger customLogger,
                                                              HItemFilter filter, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            return null;
        }

        ICustomEntity customEntity = null;
        String customEntityTitle = null;
        if(config.contains(joinPaths(key, "creeper"))) {
            customEntityTitle = String.format("creeper of %s", title);
            customEntity = HCustomCreeper.getFromConfig(config, customLogger, joinPaths(key, "creeper"), customEntityTitle);
        } else if(config.contains(joinPaths(key, "horse"))) {
            customEntityTitle = String.format("horse of %s", title);
            customEntity = HCustomHorse.getFromConfig(config, customLogger, joinPaths(key, "horse"), customEntityTitle);
        } else if(config.contains(joinPaths(key, "rabbit"))) {
            customEntityTitle = String.format("rabbit of %s", title);
            customEntity = HCustomRabbit.getFromConfig(config, customLogger, joinPaths(key, "rabbit"), customEntityTitle);
        }

        if (customEntity == null) {
            throw new InvalidConfigException(String.format("No modifications found of %s", title));
        }

        final Set<EntityType> nonPossibleEntityTypes = new HashSet<>(filter.getPossibleEntityTypes());
        nonPossibleEntityTypes.removeAll(customEntity.getPossibleEntityTypes());
                ;
        if(!nonPossibleEntityTypes.isEmpty()) {
            throw new InvalidConfigException(String.format(
                    "Possible entity types of filter of %s (%s) are wider than of %s (%s): %s",
                    title, filter.getPossibleEntityTypes(),
                    customEntityTitle, customEntity.getPossibleEntityTypes(),
                    nonPossibleEntityTypes));
        }

        return new HItemEntitySpecificAttributes(title, customEntity);
    }

    public String toString() {
        return String.format("%s", customEntity);
    }
}
