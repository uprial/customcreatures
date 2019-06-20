package com.gmail.uprial.customcreatures.schema.numerics;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderNumbers.getInt;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.EXP_DOWN;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.EXP_UP;

public class IntValueRandom extends AbstractValueRandom<Integer> {
    IntValueRandom(RandomDistributionType distributionType, Integer min, Integer max) {
        super(distributionType, min, max);
    }

    @Override
    public Integer getValue() {
        if (distributionType == EXP_DOWN) {
            return getExpRandom(max - min) + min;
        } else if (distributionType == EXP_UP) {
            return max - getExpRandom(max - min);
        } else {
            return random.nextInt((max - min) + 1) + min;
        }
    }

    public static IntValueRandom getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title,
                                               int hardMin, int hardMax) throws InvalidConfigException {
        RandomDistributionType distributionType = getDistributionTypeFromConfig(config, customLogger, key, title);
        Integer min = getInt(config, customLogger, joinPaths(key, "min"), String.format("minimum of %s", title), hardMin, hardMax);
        Integer max = getInt(config, customLogger, joinPaths(key, "max"), String.format("maximum of %s", title), hardMin, hardMax);

        return new IntValueRandom(distributionType, min, max);
    }

    private Integer getExpRandom(Integer max) {
        Double average = (max.doubleValue() + 1.0)/ 2.0;
        Double doubleValue = -average * Math.log(random.nextDouble());
        Integer value = (int)Math.round(Math.floor(doubleValue));
        if (value > max) {
            value = 0;
        }

        return value;
    }

    public String toString() {
        return String.format("IntValueRandom[distribution: %s, min: %d, max: %d]", distributionType, min, max);
    }
}
