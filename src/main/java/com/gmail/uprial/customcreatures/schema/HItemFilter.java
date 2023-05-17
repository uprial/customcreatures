package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getSet;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getStringSet;

public final class HItemFilter {
    private final Set<EntityType> entityTypes;
    private final Set<HItemTypeSet> entityTypeSets;
    private final Set<SpawnReason> spawnReasons;
    private final Set<String> worlds;
    private final Probability probability;

    private HItemFilter(Set<EntityType> entityTypes, Set<HItemTypeSet> entityTypeSets, Set<SpawnReason> spawnReasons, Set<String> worlds, Probability probability) {
        this.entityTypes = entityTypes;
        this.entityTypeSets = entityTypeSets;
        this.spawnReasons = spawnReasons;
        this.worlds = worlds;
        this.probability = probability;
    }

    public boolean isPassed(EntityType entityType, SpawnReason spawnReason, String world) {
        boolean typeNotFound = true;
        if (entityTypes != null) {
            if (entityTypes.contains(entityType)) {
                typeNotFound = false;
            }
        }
        if (entityTypeSets != null) {
            for(HItemTypeSet typeSet : entityTypeSets) {
                if(typeSet.isContains(entityType)) {
                    typeNotFound = false;
                    break;
                }
            }
        }
        if(typeNotFound) {
            return false;
        }

        if (spawnReasons != null) {
            if (! spawnReasons.contains(spawnReason)) {
                return false;
            }
        }
        if (worlds != null) {
            if(! worlds.contains(world)) {
                return false;
            }
        }
        if (probability != null) {
            if (! probability.isPassed()) {
                return false;
            }
        }

        return true;
    }

    public static HItemFilter getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        Set<EntityType> entityTypes = getSet(EntityType.class, config, customLogger,
                joinPaths(key, "types"), String.format("types of %s", title));
        Set<HItemTypeSet> entityTypeSets = getSet(HItemTypeSet.class, config, customLogger,
                joinPaths(key, "type-sets"), String.format("type sets of %s", title));
        Set<SpawnReason> spawnReasons = getSet(SpawnReason.class, config, customLogger,
                joinPaths(key, "reasons"), String.format("reasons of %s", title));
        Set<String> worlds = getStringSet(config, customLogger,
                joinPaths(key, "worlds"), String.format("worlds of %s", title));
        Probability probability = Probability.getFromConfig(config, customLogger, joinPaths(key, "probability"), String.format("probability of %s", title));
        if ((entityTypes == null) && (entityTypeSets == null) && (spawnReasons == null) && (worlds == null) && (probability == null)) {
            throw new InvalidConfigException(String.format("No restrictions found in %s", title));
        }

        return new HItemFilter(entityTypes, entityTypeSets, spawnReasons, worlds, probability);
    }

    public String toString() {
        return String.format("{types: %s, type-sets: %s, reasons: %s, probability: %s}",
                entityTypes, entityTypeSets, spawnReasons, probability);
    }
}
