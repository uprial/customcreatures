package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Random;
import java.util.Set;

import static com.gmail.uprial.customcreatures.config.ConfigReader.getInt;
import static com.gmail.uprial.customcreatures.config.ConfigReader.getSet;

public class HItemFilter {
    private static final int MAX_PERCENT = 100;

    private final Set<EntityType> entityTypes;
    private final Set<CreatureSpawnEvent.SpawnReason> spawnReasons;
    private final int probability;
    private final Random random = new Random();

    private HItemFilter(Set<EntityType> entityTypes, Set<CreatureSpawnEvent.SpawnReason> spawnReasons, int probability) {
        this.entityTypes = entityTypes;
        this.spawnReasons = spawnReasons;
        this.probability = probability;
    }

    public boolean pass(EntityType entityType, CreatureSpawnEvent.SpawnReason spawnReason) {
        if (null != entityTypes) {
            if (! entityTypes.contains(entityType))
                return false;
        }
        if (null != spawnReasons) {
            if (! spawnReasons.contains(spawnReason))
                return false;
        }
        if (MAX_PERCENT > probability) {
            if (random.nextInt(MAX_PERCENT) >= probability)
                return false;
        }

        return true;
    }

    public static HItemFilter getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String handlerName) throws InvalidConfigException {
        Set<EntityType> entityTypes = getSet(EntityType.class, config, customLogger,
                key + ".types", "types of handler", handlerName);
        Set<CreatureSpawnEvent.SpawnReason> spawnReasons = getSet(CreatureSpawnEvent.SpawnReason.class, config, customLogger,
                key + ".reasons", "reasons of handler", handlerName);
        int probability = getInt(config, customLogger, key + ".probability", "probability of handler", handlerName, 0, MAX_PERCENT, MAX_PERCENT);

        return new HItemFilter(entityTypes, spawnReasons, probability);
    }

    public String toString() {
        return String.format("[types: %s, reasons: %s, probability: %d]",
                entityTypes, spawnReasons, probability);
    }
}
