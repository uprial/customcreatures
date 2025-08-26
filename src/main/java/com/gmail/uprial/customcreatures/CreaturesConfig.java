package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.ConfigReaderNumbers;
import com.gmail.uprial.customcreatures.config.ConfigReaderSimple;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.HItem;
import com.gmail.uprial.customcreatures.schema.HValue;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.*;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.config.ConfigReaderSimple.getKey;

public final class CreaturesConfig {
    private final int timeoutInMs;
    private final IValue<Double> wolfSelectionRandomizer;
    private final Map<String,HItem> handlers;

    private CreaturesConfig(final int timeoutInMs,
                            final IValue<Double> wolfSelectionRandomizer,
                            final Map<String,HItem> handlers) {
        this.timeoutInMs = timeoutInMs;
        this.wolfSelectionRandomizer = wolfSelectionRandomizer;
        this.handlers = handlers;
    }

    public int apply(CustomCreatures plugin, CustomLogger customLogger, String handlerName) throws InvalidConfigException {
        HItem handler = handlers.get(lc(handlerName));
        if(handler == null) {
            throw new InvalidConfigException(String.format("Handler '%s' not found", handlerName));
        }

        int counter = 0;
        for(World world : plugin.getServer().getWorlds()) {
            for(LivingEntity entity : world.getEntitiesByClass(LivingEntity.class)) {
                if(handler.handleSpawn(plugin, customLogger, entity, null)) {
                    counter++;
                }
            }
        }

        return counter;
    }

    public void handleSpawn(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity, SpawnReason spawnReason) {
        int counter = 0;
        for (HItem handler : handlers.values()) {
            if(handler.handleSpawn(plugin, customLogger, entity, spawnReason)) {
                counter++;
            }
        }
        if (customLogger.isDebugMode()) {
            customLogger.debug(String.format("Handled %s spawn of %s: %d handlers applied",
                    spawnReason, format(entity), counter));
        }
    }

    public void handleDeath(CustomCreatures plugin, CustomLogger customLogger, EntityDeathEvent event, int lootBonusMobs) {
        int counter = 0;
        for (HItem handler : handlers.values()) {
            if(handler.handleDeath(plugin, customLogger, event, lootBonusMobs)) {
                counter++;
            }
        }
        if (customLogger.isDebugMode()) {
            customLogger.debug(String.format("Handled death of %s: %d handlers applied",
                    format(event.getEntity()), counter));
        }
    }

    public static boolean isDebugMode(FileConfiguration config, CustomLogger customLogger) throws InvalidConfigException {
        return ConfigReaderSimple.getBoolean(config, customLogger, "debug", "'debug' flag", false);
    }

    public int getTimeoutInMs() {
        return timeoutInMs;
    }

    public IValue<Double> getWolfSelectionRandomizer() {
        return wolfSelectionRandomizer;
    }

    public static CreaturesConfig getFromConfig(FileConfiguration config, CustomLogger customLogger) throws InvalidConfigException {
        int timeoutInMs = ConfigReaderNumbers.getInt(config, customLogger,
                "timeout-in-ms", "'timeout-in-ms' value", 1, 3600_000, 5);
        IValue<Double> wolfSelectionRandomizer = HValue.getDoubleFromConfig(config, customLogger,
                "wolf-selection-randomizer",
                "'wolf-selection-randomizer' value", 0.0D, 2.0D);

        List<?> handlersConfig = config.getList("handlers");
        if((handlersConfig == null) || (handlersConfig.size() <= 0)) {
            throw new InvalidConfigException("Empty 'handlers' list");
        }

        Map<String,HItem> handlers = new LinkedHashMap<>();
        Set<String> keys = new HashSet<>();

        int handlersConfigSize = handlersConfig.size();
        for(int i = 0; i < handlersConfigSize; i++) {
            String key = getKey(handlersConfig.get(i), "'handlers'", i);
            String keyLC = lc(key);
            if (keys.contains(keyLC)) {
                throw new InvalidConfigException(String.format("Key '%s' in 'handlers' is not unique", key));
            }
            if (config.get(key) == null) {
                throw new InvalidConfigException(String.format("Null definition of handler '%s' at pos %d", key, i));
            }
            keys.add(keyLC);

            try {
                handlers.put(keyLC, HItem.getFromConfig(config, customLogger, key));
            } catch (InvalidConfigException e) {
                customLogger.error(e.getMessage());
            }
        }

        if(handlers.size() < 1) {
            throw new InvalidConfigException("There are no valid handlers definitions");
        }

        return new CreaturesConfig(timeoutInMs, wolfSelectionRandomizer, handlers);
    }

    private static String lc(String key) {
        return key.toLowerCase(Locale.getDefault());
    }

    public String toString() {
        return String.format("timeout-in-ms: %d, wolf-selection-randomizer: %s, handlers: %s",
                timeoutInMs, wolfSelectionRandomizer, handlers.values());
    }
}
