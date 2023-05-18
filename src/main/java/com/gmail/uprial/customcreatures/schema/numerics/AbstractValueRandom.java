package com.gmail.uprial.customcreatures.schema.numerics;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getEnum;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.NORMAL;

public abstract class AbstractValueRandom<T> implements IValue<T> {
    private static final RandomDistributionType DEFAULT_DISTRIBUTION_TYPE = NORMAL;

    final RandomDistributionType distributionType;
    final Random random = new Random();

    AbstractValueRandom(RandomDistributionType distributionType) {
        this.distributionType = distributionType;
    }

    public static RandomDistributionType getDistributionTypeFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        RandomDistributionType distributionType;
        if (config.get(joinPaths(key, "distribution")) == null) {
            customLogger.debug(String.format("Empty distribution of %s. Use default value %s", title, DEFAULT_DISTRIBUTION_TYPE));
            distributionType = NORMAL;
        } else {
            distributionType = getEnum(RandomDistributionType.class, config, joinPaths(key, "distribution"), String.format("distribution type of %s", title));
        }

        return distributionType;
    }

    public static boolean is(FileConfiguration config, String key) {
        return (config.isConfigurationSection(key)
                && config.isString(joinPaths(key, "type"))
                && config.getString(joinPaths(key, "type")).equals("random"));
    }
}
