package com.gmail.uprial.customcreatures.schema.numerics;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderNumbers.getDouble;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.EXP_DOWN;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.EXP_UP;

public class DoubleValueRandom extends NumericValueRandom<Double> {
    DoubleValueRandom(RandomDistributionType distributionType, Double min, Double max) {
        super(distributionType, min, max);
    }

    @Override
    public Double getValue() {
        if (distributionType == EXP_DOWN) {
            return getExpRandom(max - min) + min;
        } else if (distributionType == EXP_UP) {
            return max - getExpRandom(max - min);
        } else {
            return (random.nextDouble() * (max - min)) + min;
        }
    }

    public static DoubleValueRandom getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title,
                                                  double hardMin, double hardMax) throws InvalidConfigException {
        final RandomDistributionType distributionType = getDistributionTypeFromConfig(config, customLogger, key, title);
        final Double min = getDouble(config, customLogger, joinPaths(key, "min"), String.format("minimum of %s", title), hardMin, hardMax);
        final Double max = getDouble(config, customLogger, joinPaths(key, "max"), String.format("maximum of %s", title), hardMin, hardMax);

        return new DoubleValueRandom(distributionType, min, max);
    }

    private Double getExpRandom(final Double max) {
        final double average = max / 2.0D;
        double value = -average * Math.log(random.nextDouble());
        if (value > max) {
            value = 0.0D;
        }

        return value;
    }

    public String toString() {
        return String.format("DoubleValueRandom{distribution: %s, min: %.2f, max: %.2f}", distributionType, min, max);
    }
}
