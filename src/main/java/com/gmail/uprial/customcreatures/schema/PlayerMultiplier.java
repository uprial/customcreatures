package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.enums.PlayerSort;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

import static com.gmail.uprial.customcreatures.common.DoubleHelper.MAX_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.common.DoubleHelper.MIN_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.*;

public class PlayerMultiplier {
    private final PlayerSort sort;
    private final Statistic statistic;
    private final IValue<Double> divider;
    private final IValue<Double> max;

    private static final double DEFAULT_MULTIPLIER = 1.0D;
    private static final double EMPTY_STATISTIC = 0.0D;

    private PlayerMultiplier(PlayerSort sort, Statistic statistic, IValue<Double> divider, IValue<Double> max) {
        this.sort = sort;
        this.statistic = statistic;
        this.divider = divider;
        this.max = max;
    }

    private boolean isNull() {
        return (sort == null || statistic == null || divider == null || max == null);
    }

    private static UUID cachedEntityUUID;
    private static Collection<Player> cachedPlayers;

    private Collection<Player> getPlayers(LivingEntity entity) {
        if(!entity.getUniqueId().equals(cachedEntityUUID)) {
            cachedEntityUUID = entity.getUniqueId();
            cachedPlayers = entity.getWorld().getEntitiesByClass(Player.class);
        }

        return cachedPlayers;
    }

    private double getBiggestStatistic(LivingEntity entity) {
        double value = EMPTY_STATISTIC;

        for(Player player : getPlayers(entity)) {
            value = Math.max(value, player.getStatistic(statistic));
        }

        return value;
    }

    private double getClosestStatistic(LivingEntity entity) {
        Double closestDistance = null;
        Player closestPlayer = null;
        for(Player player : getPlayers(entity)) {
            final double playerDistance = player.getLocation().distance(entity.getLocation());
            if(closestDistance == null || playerDistance < closestDistance) {
                closestPlayer = player;
                closestDistance = playerDistance;
            }
        }

        if(closestPlayer == null) {
            return EMPTY_STATISTIC;
        } else {
            return closestPlayer.getStatistic(statistic);
        }
    }

    public double get(LivingEntity entity) {
        if(isNull()) {
            return DEFAULT_MULTIPLIER;
        } else {
            final double value;
            if(sort == PlayerSort.BIGGEST) {
                value = getBiggestStatistic(entity);
            } else { // CLOSEST
                value = getClosestStatistic(entity);
            }

            return Math.min(max.getValue(), DEFAULT_MULTIPLIER + value / divider.getValue());
        }
    }

    public static PlayerMultiplier getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return new PlayerMultiplier(null, null, null, null);
        }

        final PlayerSort sort = getEnum(PlayerSort.class, config,
                joinPaths(key, "sort"), String.format("sort of %s", title));
        final Statistic statistic = getEnum(Statistic.class, config,
                joinPaths(key, "statistic"), String.format("statistic of %s", title));

        if (config.get(joinPaths(key, "divider")) == null) {
            throw new InvalidConfigException(String.format("Null divider of %s", title));
        }

        final IValue<Double> divider = HValue.getDoubleFromConfig(config, customLogger,
                joinPaths(key, "divider"),
                String.format("divider of %s", title), MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE);

        if (config.get(joinPaths(key, "max")) == null) {
            throw new InvalidConfigException(String.format("Null max of %s", title));
        }

        final IValue<Double> max = HValue.getDoubleFromConfig(config, customLogger,
                joinPaths(key, "max"),
                String.format("max of %s", title), MIN_DOUBLE_VALUE, MAX_DOUBLE_VALUE);

        return new PlayerMultiplier(sort, statistic, divider, max);
    }

    public String toString() {
        if(isNull()) {
            return "null";
        } else {
            return String.format("{sort: %s, statistic: %s, divider: %s, max: %s}",
                    sort, statistic, divider, max);
        }
    }
}
