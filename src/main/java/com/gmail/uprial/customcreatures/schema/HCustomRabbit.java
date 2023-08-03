package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;

import java.util.HashSet;
import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getEnumOrDefault;

public final class HCustomRabbit implements ICustomEntity {
    private static final Set<EntityType> POSSIBLE_ENTITY_TYPES = new HashSet<EntityType>(){{ add(EntityType.RABBIT); }};

    private final String title;
    private final Rabbit.Type type;

    private HCustomRabbit(String title, Rabbit.Type type) {
        this.title = title;
        this.type = type;
    }

    @Override
    public void apply(CustomLogger customLogger, Entity entity) {
        final Rabbit rabbit = (Rabbit)entity;
        applyType(customLogger, rabbit);
    }

    public void applyType(CustomLogger customLogger, Rabbit rabbit) {
        if (type != null) {
            rabbit.setRabbitType(type);
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: set type of %s to %s",
                        title, format(rabbit), type));
            }
        }
    }

    @Override
    public Set<EntityType> getPossibleEntityTypes() {
        return POSSIBLE_ENTITY_TYPES;
    }

    public static HCustomRabbit getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        Rabbit.Type type = getEnumOrDefault(Rabbit.Type.class, config, customLogger,
                joinPaths(key, "type"), String.format("type of %s", title), null);

        if (type == null) {
            throw new InvalidConfigException(String.format("No modifications found of %s", title));
        }

        return new HCustomRabbit(title, type);
    }

    public String toString() {
        return String.format("Rabbit{type: %s}", type);
    }
}
