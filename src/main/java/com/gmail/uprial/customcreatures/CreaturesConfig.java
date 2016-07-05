package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.config.ConfigReader;
import com.gmail.uprial.customcreatures.schema.HItem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreaturesConfig {
    private List<HItem> handlers;

    public CreaturesConfig(FileConfiguration config, CustomLogger customLogger) throws InvalidConfigException {
        readConfig(config, customLogger);
    }

    public void handle(CustomLogger customLogger, LivingEntity entity, CreatureSpawnEvent.SpawnReason spawnReason) {
        for (HItem handler : handlers) {
            handler.handle(customLogger, entity, spawnReason);
        }
    }

    private void readConfig(FileConfiguration config, CustomLogger customLogger) throws InvalidConfigException {
        boolean debug = ConfigReader.getBoolean(config, customLogger, "debug", "'debug' flag", false);
        customLogger.setDebugMode(debug);

        List<?> handlersConfig = config.getList("handlers");
        if((null == handlersConfig) || (handlersConfig.size() <= 0)) {
            throw new InvalidConfigException("Empty 'handlers' list");
        }

        handlers = new ArrayList<>();
        Map<String,Integer> keys = new HashMap<>();

        for(int i = 0; i < handlersConfig.size(); i++) {
            Object item = handlersConfig.get(i);
            if (null == item) {
                throw new InvalidConfigException(String.format("Null key in 'handlers' at pos %d", i));
            }
            if (! (item instanceof String)) {
                throw new InvalidConfigException(String.format("Key '%s' in 'handlers' at pos %d is not a string", item.toString(), i));
            }
            String key = item.toString();
            if (key.length() < 1) {
                throw new InvalidConfigException(String.format("Empty key in 'handlers' at pos %d", i));
            }
            if (keys.containsKey(key.toLowerCase())) {
                throw new InvalidConfigException(String.format("Key '%s' in 'handlers' is not unique", key));
            }
            if (null == config.get(key)) {
                throw new InvalidConfigException(String.format("Null definition of handler '%s' at pos %d", key, i));
            }
            keys.put(key.toLowerCase(), 1);

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
