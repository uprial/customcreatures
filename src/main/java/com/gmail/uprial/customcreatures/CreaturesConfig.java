package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.ConfigReaderSimple;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.HItem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.*;

import static com.gmail.uprial.customcreatures.config.ConfigReaderSimple.getKey;

public final class CreaturesConfig {
    private final List<HItem> handlers;

    private CreaturesConfig(List<HItem> handlers) {
        this.handlers = handlers;
    }

    public void handle(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity, SpawnReason spawnReason) {
        for (HItem handler : handlers) {
            handler.handle(plugin, customLogger, entity, spawnReason);
        }
    }

    public static boolean isDebugMode(FileConfiguration config, CustomLogger customLogger) throws InvalidConfigException {
        return ConfigReaderSimple.getBoolean(config, customLogger, "debug", "'debug' flag", false);
    }

    public static CreaturesConfig getFromConfig(FileConfiguration config, CustomLogger customLogger) throws InvalidConfigException {
        List<?> handlersConfig = config.getList("handlers");
        if((handlersConfig == null) || (handlersConfig.size() <= 0)) {
            throw new InvalidConfigException("Empty 'handlers' list");
        }

        List<HItem> handlers = new ArrayList<>();
        Set<String> keys = new HashSet<>();

        int handlersConfigSize = handlersConfig.size();
        for(int i = 0; i < handlersConfigSize; i++) {
            String key = getKey(handlersConfig.get(i), "'handlers'", i);
            String keyLC = key.toLowerCase(Locale.getDefault());
            if (keys.contains(keyLC)) {
                throw new InvalidConfigException(String.format("Key '%s' in 'handlers' is not unique", key));
            }
            if (config.get(key) == null) {
                throw new InvalidConfigException(String.format("Null definition of handler '%s' at pos %d", key, i));
            }
            keys.add(keyLC);

            try {
                handlers.add(HItem.getFromConfig(config, customLogger, key));
            } catch (InvalidConfigException e) {
                customLogger.error(e.getMessage());
            }
        }

        if(handlers.size() < 1) {
            throw new InvalidConfigException("There are no valid handlers definitions");
        }

        return new CreaturesConfig(handlers);
    }

    public String toString() {
        return String.format("handlers: %s", handlers.toString());
    }
}
