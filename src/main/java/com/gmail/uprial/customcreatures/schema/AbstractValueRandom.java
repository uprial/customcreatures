package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReader.getEnum;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.NORMAL;

abstract public class AbstractValueRandom<T> implements IValue<T> {
    private static final RandomDistributionType defaultDistributionType = NORMAL;

    protected final RandomDistributionType distributionType;
    protected final T min;
    protected final T max;
    protected final Random random = new Random();

    AbstractValueRandom(RandomDistributionType distributionType, T min, T max) {
        this.distributionType = distributionType;
        this.min = min;
        this.max = max;
    }

    public static RandomDistributionType getDistributionTypeFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        RandomDistributionType distributionType;
        if (null == config.get(joinPaths(key, "distribution"))) {
            customLogger.debug(String.format("Empty distribution of %s. Use default value %s", title, defaultDistributionType));
            distributionType = NORMAL;
        } else
            distributionType = getEnum(RandomDistributionType.class, config, joinPaths(key, "distribution"), "distribution type of " + title);

        return distributionType;
    }

    public static boolean is(FileConfiguration config, String key) {
        return (config.isConfigurationSection(key)
                && config.isString(joinPaths(key, "type"))
                && config.getString(joinPaths(key, "type")).equals("random"));
    }
}
