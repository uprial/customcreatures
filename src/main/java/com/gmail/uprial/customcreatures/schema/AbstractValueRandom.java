package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import static com.gmail.uprial.customcreatures.config.ConfigReader.getEnum;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.NORMAL;

abstract public class AbstractValueRandom {
    private static final RandomDistributionType defaultDistributionType = NORMAL;

    public static RandomDistributionType getDistributionTypeFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title, String handlerName) throws InvalidConfigException {
        RandomDistributionType distributionType;
        if (null == config.get(key + ".distribution")) {
            customLogger.debug(String.format("Empty distribution of %s '%s'. Use default value %s", title, handlerName, defaultDistributionType));
            distributionType = NORMAL;
        } else
            distributionType = getEnum(RandomDistributionType.class, config, key + ".distribution", "distribution type of", key);

        return distributionType;
    }

    public static boolean is(FileConfiguration config, String key) {
        return (config.isConfigurationSection(key)
                && config.isString(key + ".type")
                && config.getString(key + ".type").equals("random"));
    }
}
