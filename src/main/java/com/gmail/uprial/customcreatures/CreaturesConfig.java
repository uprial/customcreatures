package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.ConfigReaderNumbers;
import com.gmail.uprial.customcreatures.config.ConfigReaderSimple;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.HItem;
import com.gmail.uprial.customcreatures.schema.Breeder;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.*;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.config.ConfigReaderSimple.getKey;

public final class CreaturesConfig {
    private final int timeoutInMs;
    private final Map<String,Breeder> breeders;
    private final Map<String,HItem> handlers;

    private CreaturesConfig(final int timeoutInMs,
                            final Map<String,Breeder> breeders,
                            final Map<String,HItem> handlers) {
        this.timeoutInMs = timeoutInMs;
        this.breeders = breeders;
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

    public void handleBreed(CustomLogger customLogger, EntityBreedEvent event) {
        int counter = 0;
        for (Breeder breeder : breeders.values()) {
            if(breeder.handleBreed(customLogger, event)) {
                counter++;
            }
        }
        if (customLogger.isDebugMode()) {
            customLogger.debug(String.format("Handled breed of %s: %d handlers applied",
                    format(event.getEntity()), counter));
        }
    }

    public static boolean isDebugMode(FileConfiguration config, CustomLogger customLogger) throws InvalidConfigException {
        return ConfigReaderSimple.getBoolean(config, customLogger, "debug", "'debug' flag", false);
    }

    public int getTimeoutInMs() {
        return timeoutInMs;
    }

    public static CreaturesConfig getFromConfig(FileConfiguration config, CustomLogger customLogger) throws InvalidConfigException {
        int timeoutInMs = ConfigReaderNumbers.getInt(config, customLogger,
                "timeout-in-ms", "'timeout-in-ms' value", 1, 3600_000, 5);

        final Map<String,Breeder> breeders = getList(config, customLogger, "breeders", "'breeders'",
                (String key) -> Breeder.getFromConfig(config, customLogger, key, String.format("breeder '%s'", key)));

        final Map<String,HItem> handlers = getList(config, customLogger, "handlers", "'handlers'",
                (String key) -> HItem.getFromConfig(config, customLogger, key));

        return new CreaturesConfig(timeoutInMs, breeders, handlers);
    }

    private interface ListGetter<T> {
        T get(final String key) throws InvalidConfigException;
    }
    private static <T> Map<String, T> getList(FileConfiguration config, CustomLogger customLogger,
                                              String key, String title,
                                              ListGetter<T> func) throws InvalidConfigException {

        List<?> itemsConfig = config.getList(key);
        if((itemsConfig == null) || (itemsConfig.size() <= 0)) {
            throw new InvalidConfigException(String.format("Empty %s list", title));
        }

        Map<String,T> items = new LinkedHashMap<>();
        Set<String> keys = new HashSet<>();

        int itemsConfigConfigSize = itemsConfig.size();
        for(int i = 0; i < itemsConfigConfigSize; i++) {
            String subKey = getKey(itemsConfig.get(i), title, i);
            String subKeyLC = lc(subKey);
            if (keys.contains(subKeyLC)) {
                throw new InvalidConfigException(String.format("Key '%s' in %s is not unique", subKey, title));
            }
            if (config.get(subKey) == null) {
                throw new InvalidConfigException(String.format("Null definition of '%s' for %s at pos %d", subKey, title, i));
            }
            keys.add(subKeyLC);

            try {
                items.put(subKeyLC, func.get(subKey));
            } catch (InvalidConfigException e) {
                customLogger.error(e.getMessage());
            }
        }

        if(items.size() < 1) {
            throw new InvalidConfigException(String.format("There are no valid %s definitions", title));
        }

        return items;
    }

    private static String lc(String key) {
        return key.toLowerCase(Locale.getDefault());
    }

    public String toString() {
        return String.format("timeout-in-ms: %d, breeders: %s, handlers: %s",
                timeoutInMs, breeders.values(), handlers.values());
    }
}
