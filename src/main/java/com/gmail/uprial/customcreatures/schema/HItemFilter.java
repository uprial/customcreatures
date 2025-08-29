package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getSet;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getStringSet;

public final class HItemFilter extends EntityTypeFilter {
    private final Set<SpawnReason> spawnReasons;
    private final Set<SpawnReason> excludeSpawnReasons;
    private final Set<String> worlds;
    private final Probability probability;
    private final PlayerMultiplier probabilityPlayerMultiplier;

    private HItemFilter(final EntityTypeFilter entityTypeFilter,
                        final Set<SpawnReason> spawnReasons,
                        final Set<SpawnReason> excludeSpawnReasons,
                        final Set<String> worlds,
                        final Probability probability,
                        final PlayerMultiplier probabilityPlayerMultiplier) {
        super(entityTypeFilter);

        this.spawnReasons = spawnReasons;
        this.excludeSpawnReasons = excludeSpawnReasons;
        this.worlds = worlds;
        this.probability = probability;
        this.probabilityPlayerMultiplier = probabilityPlayerMultiplier;
    }

    public boolean isPassed(LivingEntity entity, SpawnReason spawnReason, String world) {
        if(!super.isPassed(entity)) {
            return false;
        }

        // spawnReason may be null if called from CreaturesConfig.apply()
        if (spawnReason != null) {
            if (spawnReasons != null) {
                if (!spawnReasons.contains(spawnReason)) {
                    return false;
                }
            }
            if (excludeSpawnReasons != null) {
                if (excludeSpawnReasons.contains(spawnReason)) {
                    return false;
                }
            }
        }
        if (worlds != null) {
            if(! worlds.contains(world)) {
                return false;
            }
        }
        if (probability != null) {
            if (! probability.isPassedWithMult(probabilityPlayerMultiplier.get(entity))) {
                return false;
            }
        }

        return true;
    }

    public static HItemFilter getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        final EntityTypeFilter entityTypeFilter = EntityTypeFilter.getFromConfig(config, customLogger, key, title);

        final Set<SpawnReason> spawnReasons = getSet(SpawnReason.class, config, customLogger,
                joinPaths(key, "reasons"), String.format("reasons of %s", title));
        final Set<SpawnReason> excludeSpawnReasons = getSet(SpawnReason.class, config, customLogger,
                joinPaths(key, "exclude-reasons"), String.format("exclude reasons of %s", title));
        final Set<String> worlds = getStringSet(config, customLogger,
                joinPaths(key, "worlds"), String.format("worlds of %s", title));
        final Probability probability = Probability.getFromConfig(config, customLogger,
                joinPaths(key, "probability"), String.format("probability of %s", title));
        final PlayerMultiplier probabilityPlayerMultiplier = PlayerMultiplier.getFromConfig(config, customLogger,
                joinPaths(key, "probability-player-multiplier"), String.format("probability player multiplier of %s", title));

        return new HItemFilter(entityTypeFilter,
                spawnReasons, excludeSpawnReasons,
                worlds, probability, probabilityPlayerMultiplier);
    }

    public String toString() {
        return String.format("{types: %s, exclude-types: %s," +
                        " type-sets: %s, exclude-type-sets: %s," +
                        " reasons: %s, exclude-reasons: %s, " +
                        "probability: %s, probability-player-multiplier: %s}",
                entityTypes, excludeEntityTypes,
                entityTypeSets, excludeEntityTypeSets,
                spawnReasons, excludeSpawnReasons,
                probability, probabilityPlayerMultiplier);
    }
}
