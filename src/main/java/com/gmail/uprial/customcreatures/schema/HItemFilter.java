package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getSet;

public final class HItemFilter {
    private final Set<EntityType> entityTypes;
    private final Set<CreatureSpawnEvent.SpawnReason> spawnReasons;
    private final Probability probability;

    private HItemFilter(Set<EntityType> entityTypes, Set<CreatureSpawnEvent.SpawnReason> spawnReasons, Probability probability) {
        this.entityTypes = entityTypes;
        this.spawnReasons = spawnReasons;
        this.probability = probability;
    }

    public boolean pass(EntityType entityType, CreatureSpawnEvent.SpawnReason spawnReason) {
        if (entityTypes != null) {
            if (! entityTypes.contains(entityType))
                return false;
        }
        if (spawnReasons != null) {
            if (! spawnReasons.contains(spawnReason))
                return false;
        }
        if (probability != null) {
            if (! probability.pass())
                return false;
        }

        return true;
    }

    public static HItemFilter getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        Set<EntityType> entityTypes = getSet(EntityType.class, config, customLogger,
                joinPaths(key, "types"), String.format("types of %s", title));
        Set<CreatureSpawnEvent.SpawnReason> spawnReasons = getSet(CreatureSpawnEvent.SpawnReason.class, config, customLogger,
                joinPaths(key, "reasons"), String.format("reasons of %s", title));
        Probability probability = Probability.getFromConfig(config, customLogger, joinPaths(key, "probability"), String.format("probability of %s", title));
        if ((entityTypes == null) && (spawnReasons == null) && (probability == null)) {
            throw new InvalidConfigException(String.format("No restrictions found in %s", title));
        }

        return new HItemFilter(entityTypes, spawnReasons, probability);
    }

    public String toString() {
        return String.format("[types: %s, reasons: %s, probability: %s]",
                entityTypes, spawnReasons, probability);
    }
}
