package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.ConfigReaderSimple;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.HItem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.*;

import static com.gmail.uprial.customcreatures.config.ConfigReaderLists.getKey;

public class CreaturesConfig {
    private List<HItem> handlers;

    public CreaturesConfig(FileConfiguration config, CustomLogger customLogger) throws InvalidConfigException {
        readConfig(config, customLogger);
    }

    public void handle(CustomLogger customLogger, LivingEntity entity, SpawnReason spawnReason) {
        for (HItem handler : handlers) {
            handler.handle(customLogger, entity, spawnReason);
        }
    }

    private void readConfig(FileConfiguration config, CustomLogger customLogger) throws InvalidConfigException {
        boolean debug = ConfigReaderSimple.getBoolean(config, customLogger, "debug", "'debug' flag", false);
        customLogger.setDebugMode(debug);

        List<?> handlersConfig = config.getList("handlers");
        if((handlersConfig == null) || (handlersConfig.size() <= 0)) {
            throw new InvalidConfigException("Empty 'handlers' list");
        }

        handlers = new ArrayList<>();
        Map<String,Integer> keys = new HashMap<>();

        for(int i = 0; i < handlersConfig.size(); i++) {
            String key = getKey(handlersConfig.get(i), "'handlers'", i);
            String keyLC = key.toLowerCase(Locale.getDefault());
            if (keys.containsKey(keyLC)) {
                throw new InvalidConfigException(String.format("Key '%s' in 'handlers' is not unique", key));
            }
            if (config.get(key) == null) {
                throw new InvalidConfigException(String.format("Null definition of handler '%s' at pos %d", key, i));
            }
            keys.put(keyLC, 1);

            try {
                handlers.add(HItem.getFromConfig(config, customLogger, key));
            } catch (InvalidConfigException e) {
                customLogger.error(e.getMessage());
            }
        }

        if(handlers.size() < 1) {
            throw new InvalidConfigException("There are no valid handlers definitions");
        }
    }
}
