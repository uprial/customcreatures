package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.ZombieHorse;

import java.util.HashSet;
import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getEnumOrDefault;

public final class HCustomZombieHorse implements ICustomEntity {
    private static final Set<EntityType> POSSIBLE_ENTITY_TYPES = new HashSet<EntityType>(){{ add(EntityType.ZOMBIE_HORSE); }};

    private final String title;
    private final IValue<Double> jumpStrength;
    private final IValue<Boolean> tamed;

    private HCustomZombieHorse(String title, IValue<Double> jumpStrength,
                               IValue<Boolean> tamed) {
        this.title = title;
        this.jumpStrength = jumpStrength;
        this.tamed = tamed;
    }

    @Override
    public void apply(CustomLogger customLogger, Entity entity) {
        final ZombieHorse horse = (ZombieHorse) entity;
        applyJumpStrength(customLogger, horse);
        applyTamed(customLogger, horse);
    }

    public void applyJumpStrength(CustomLogger customLogger, ZombieHorse horse) {
        if (jumpStrength != null) {
            final Double oldValue = horse.getJumpStrength();
            final Double newValue = jumpStrength.getValue();
            horse.setJumpStrength(newValue);
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: set jump strength of %s from %.2f to %.2f",
                        title, format(horse), oldValue, newValue));
            }
        }
    }

    public void applyTamed(CustomLogger customLogger, ZombieHorse horse) {
        if (tamed != null) {
            final Boolean oldValue = horse.isTamed();
            final Boolean newValue = tamed.getValue();
            horse.setTamed(newValue);
            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: set 'tamed' flag of %s from %b to %b",
                        title, format(horse), oldValue, newValue));
            }
        }
    }

    @Override
    public Set<EntityType> getPossibleEntityTypes() {
        return POSSIBLE_ENTITY_TYPES;
    }

    public static HCustomZombieHorse getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        /*
            https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/AbstractHorse.html#setJumpStrength(double)
            You cannot set a jump strength to a value below 0 or above 2.
         */
        IValue<Double> jumpStrength = HValue.getDoubleFromConfig(config, customLogger, joinPaths(key, "jump-strength"),
                String.format("jump strength of %s", title), 0.0D, 2.0D);

        IValue<Boolean> tamed = HValue.getBooleanFromConfig(config, customLogger, joinPaths(key, "tamed"),
                String.format("'tamed' flag of %s", title));

        if ((jumpStrength == null)
                && (tamed == null)) {
            throw new InvalidConfigException(String.format("No modifications found of %s", title));
        }

        return new HCustomZombieHorse(title, jumpStrength, tamed);
    }

    public String toString() {
        return String.format("Horse{jump-strength: %s, tamed: %s}",
                jumpStrength, tamed);
    }
}
