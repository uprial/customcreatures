package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

import static com.gmail.uprial.customcreatures.config.ConfigReader.getDouble;
import static com.gmail.uprial.customcreatures.config.ConfigReader.getEnum;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.EXP_DOWN;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.EXP_UP;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.NORMAL;

public class ValueRandom implements IValue {
    private static final RandomDistributionType defaultDistributionType = NORMAL;

    protected final RandomDistributionType distributionType;
    protected final double min;
    protected final double max;
    private final Random random = new Random();

    ValueRandom(RandomDistributionType distributionType, double min, double max) {
        this.distributionType = distributionType;
        this.min = min;
        this.max = max;
    }

    @Override
    public double getValue() {
        if (distributionType == EXP_DOWN) {
            return getExpRandom(max - min) + min;
        } else if (distributionType == EXP_UP) {
            return max - getExpRandom(max - min);
        } else
            return random.nextDouble() * (max - min) + min;
    }

    public static boolean is(FileConfiguration config, String key) {
        return (config.isConfigurationSection(key)
                && config.isString(key + ".type")
                && config.getString(key + ".type").equals("random"));
    }

    public static ValueRandom getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title, String handlerName) throws InvalidConfigException {
        RandomDistributionType distributionType;
        if (null == config.get(key + ".distribution")) {
            customLogger.debug(String.format("Empty distribution of %s '%s'. Use default value %s", title, handlerName, defaultDistributionType));
            distributionType = NORMAL;
        } else
            distributionType = getEnum(RandomDistributionType.class, config, key + ".distribution", "distribution type of", key);

        double min = getDouble(config, key + ".min", String.format("minimum of %s", title), handlerName);
        double max = getDouble(config, key + ".max", String.format("maximum of %s", title), handlerName);

        return new ValueRandom(distributionType, min, max);
    }

    private double getExpRandom(double max) {
        double average = max / 2.0;
        double value = -average * Math.log(random.nextDouble());
        if (value > max)
            value = 0.0;

        return value;
    }
}