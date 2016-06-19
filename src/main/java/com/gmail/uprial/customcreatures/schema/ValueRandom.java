package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

import static com.gmail.uprial.customcreatures.config.ConfigReader.getEnum;
import static com.gmail.uprial.customcreatures.config.ConfigReader.getInt;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.EXP_DOWN;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.EXP_UP;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.NORMAL;

public class ValueRandom extends Value {
    private static final RandomDistributionType defaultDistributionType = NORMAL;

    protected final RandomDistributionType distributionType;
    protected final int min;
    protected final int max;
    private final Random random = new Random();

    ValueRandom(RandomDistributionType distributionType, int min, int max) {
        this.distributionType = distributionType;
        this.min = min;
        this.max = max;
    }

    @Override
    public int getValue() {
        if (distributionType == EXP_UP) {
            return getExpRandom((max - min) / 2) + min;
        } else if (distributionType == EXP_DOWN) {
            return max - getExpRandom((max - min) / 2);
        } else
            return random.nextInt(max - min + 1) + min;
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

        int min = getInt(config, key + ".min", String.format("minimum of %s", title), handlerName);
        int max = getInt(config, key + ".max", String.format("maximum of %s", title), handlerName);

        return new ValueRandom(distributionType, min, max);
    }

    private int getExpRandom(float average) {
        int random = (int)Math.round(Math.floor(-average * Math.log(Math.random())));
        if (random > average * 2)
            random = 0;

        return random;
    }
}