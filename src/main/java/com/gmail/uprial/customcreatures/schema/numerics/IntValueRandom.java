package com.gmail.uprial.customcreatures.schema.numerics;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderNumbers.getInt;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.EXP_DOWN;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.EXP_UP;

public class IntValueRandom extends NumericValueRandom<Integer> {
    public IntValueRandom(RandomDistributionType distributionType, Integer min, Integer max) {
        super(distributionType, min, max);
    }

    @Override
    public Integer getValue() {
        return getRandom(min, max);
    }

    public Integer getValueWithInc(Integer incMin, Integer incMax) {
        return getRandom(min + incMin, max + incMax);
    }

    public static IntValueRandom getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title,
                                               int hardMin, int hardMax) throws InvalidConfigException {
        RandomDistributionType distributionType = getDistributionTypeFromConfig(config, customLogger, key, title);
        Integer min = getInt(config, customLogger, joinPaths(key, "min"), String.format("minimum of %s", title), hardMin, hardMax);
        Integer max = getInt(config, customLogger, joinPaths(key, "max"), String.format("maximum of %s", title), hardMin, hardMax);

        return new IntValueRandom(distributionType, min, max);
    }

    private Integer getRandom(Integer min, Integer max) {
        if (distributionType == EXP_DOWN) {
            return getExpRandom(max - min) + min;
        } else if (distributionType == EXP_UP) {
            return max - getExpRandom(max - min);
        } else {
            return random.nextInt(Math.abs(max - min) + 1) * Integer.signum(max - min) + min;
        }
    }

    private Integer getExpRandom(final Integer max) {
        double average = (max.doubleValue() + 1.0) / 2.0;
        double doubleValue = -average * Math.log(random.nextDouble());
        int value = (int)Math.round(Math.floor(doubleValue));
        if (value > max) {
            value = 0;
        }

        return value;
    }

    public String toString() {
        return String.format("IntValueRandom{distribution: %s, min: %d, max: %d}", distributionType, min, max);
    }
}
