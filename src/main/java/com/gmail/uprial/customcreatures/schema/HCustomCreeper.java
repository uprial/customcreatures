package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.HashSet;
import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;

public final class HCustomCreeper implements ICustomEntity {
    private static final Set<EntityType> POSSIBLE_ENTITY_TYPES = new HashSet<EntityType>(){{ add(EntityType.CREEPER); }};

    private final String title;
    private final IValue<Integer> maxFuseTicks;
    private final IValue<Integer> explosionRadius;
    private final IValue<Boolean> powered;

    private HCustomCreeper(String title, IValue<Integer> maxFuseTicks, IValue<Integer> explosionRadius, IValue<Boolean> powered) {
        this.title = title;
        this.maxFuseTicks = maxFuseTicks;
        this.explosionRadius = explosionRadius;
        this.powered = powered;
    }

    @Override
    public void apply(CustomLogger customLogger, Entity entity) {
        final Creeper creeper = (Creeper)entity;
        applyMaxFuseTicks(customLogger, creeper);
        applyExplosionRadius(customLogger, creeper);
        applyPowered(customLogger, creeper);
    }

    public void applyMaxFuseTicks(CustomLogger customLogger, Creeper creeper) {
        if (maxFuseTicks != null) {
            final Integer oldValue = creeper.getMaxFuseTicks();
            final Integer newValue = maxFuseTicks.getValue();
            creeper.setMaxFuseTicks(newValue);
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: set max. fuse ticks of %s from %d to %d",
                        title, format(creeper), oldValue, newValue));
            }
        }
    }

    public void applyExplosionRadius(CustomLogger customLogger, Creeper creeper) {
        if (explosionRadius != null) {
            final Integer oldValue = creeper.getExplosionRadius();
            final Integer newValue = explosionRadius.getValue();
            creeper.setExplosionRadius(newValue);
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: set explosion radius of %s from %d to %d",
                        title, format(creeper), oldValue, newValue));
            }
        }
    }

    public void applyPowered(CustomLogger customLogger, Creeper creeper) {
        if (powered != null) {
            final Boolean oldValue = creeper.isPowered();
            final Boolean newValue = powered.getValue();
            creeper.setPowered(newValue);
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: set 'powered' flag of %s from %b to %b",
                        title, format(creeper), oldValue, newValue));
            }
        }
    }

    @Override
    public Set<EntityType> getPossibleEntityTypes() {
        return POSSIBLE_ENTITY_TYPES;
    }

    public static HCustomCreeper getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        // 0 leads to an immediate explosion
        IValue<Integer> maxFuseTicks = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "max-fuse-ticks"),
                String.format("max. fuse ticks of %s", title), 1, Integer.MAX_VALUE);
        // 17 and more doesn't work due to griefing limitation
        IValue<Integer> explosionRadius = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "explosion-radius"),
                String.format("explosion radius of %s", title), 0, 16);
        IValue<Boolean> powered = HValue.getBooleanFromConfig(config, customLogger, joinPaths(key, "powered"),
                String.format("'powered' flag of %s", title));

        if ((maxFuseTicks == null)
                && (explosionRadius == null)
                && (powered == null)) {
            throw new InvalidConfigException(String.format("No modifications found of %s", title));
        }

        return new HCustomCreeper(title, maxFuseTicks, explosionRadius, powered);
    }

    public String toString() {
        return String.format("Creeper{max-fuse-ticks: %s, explosion-radius: %s, powered: %s}",
                maxFuseTicks, explosionRadius, powered);
    }
}
