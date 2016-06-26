package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

import static com.gmail.uprial.customcreatures.config.ConfigReader.getDouble;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.*;

public class DoubleValueRandom extends AbstractValueRandom implements IDoubleValue {
    private static final RandomDistributionType defaultDistributionType = NORMAL;

    protected final RandomDistributionType distributionType;
    protected final double min;
    protected final double max;
    private final Random random = new Random();

    DoubleValueRandom(RandomDistributionType distributionType, double min, double max) {
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

    public static DoubleValueRandom getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title, String handlerName) throws InvalidConfigException {
        RandomDistributionType distributionType = getDistributionTypeFromConfig(config, customLogger, key, title, handlerName);
        double min = getDouble(config, key + ".min", String.format("minimum of %s", title), handlerName);
        double max = getDouble(config, key + ".max", String.format("maximum of %s", title), handlerName);

        return new DoubleValueRandom(distributionType, min, max);
    }

    private double getExpRandom(double max) {
        double average = max / 2.0;
        double value = -average * Math.log(random.nextDouble());
        if (value > max)
            value = 0.0;

        return value;
    }
}