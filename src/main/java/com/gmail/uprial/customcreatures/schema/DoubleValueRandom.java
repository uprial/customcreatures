package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReader.getDouble;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.EXP_DOWN;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.EXP_UP;

public class DoubleValueRandom extends AbstractValueRandom<Double> {
    DoubleValueRandom(RandomDistributionType distributionType, Double min, Double max) {
        super(distributionType, min, max);
    }

    @Override
    public Double getValue() {
        if (distributionType == EXP_DOWN) {
            return getExpRandom(max - min) + min;
        } else if (distributionType == EXP_UP) {
            return max - getExpRandom(max - min);
        } else
            return random.nextDouble() * (max - min) + min;
    }

    public static DoubleValueRandom getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title, String handlerName) throws InvalidConfigException {
        RandomDistributionType distributionType = getDistributionTypeFromConfig(config, customLogger, key, title, handlerName);
        Double min = getDouble(config, joinPaths(key, "min"), String.format("minimum of %s", title), handlerName);
        Double max = getDouble(config, joinPaths(key, "max"), String.format("maximum of %s", title), handlerName);

        return new DoubleValueRandom(distributionType, min, max);
    }

    private Double getExpRandom(Double max) {
        Double average = max / 2.0;
        Double value = -average * Math.log(random.nextDouble());
        if (value > max)
            value = 0.0;

        return value;
    }
}