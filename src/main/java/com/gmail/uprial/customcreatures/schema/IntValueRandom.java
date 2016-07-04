package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReader.getInt;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.EXP_DOWN;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.EXP_UP;

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
        } else
            return random.nextInt(max - min + 1) + min;
    }

    public static IntValueRandom getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        RandomDistributionType distributionType = getDistributionTypeFromConfig(config, customLogger, key, title);
        Integer min = getInt(config, joinPaths(key, "min"), String.format("minimum of %s", title));
        Integer max = getInt(config, joinPaths(key, "max"), String.format("maximum of %s", title));

        return new IntValueRandom(distributionType, min, max);
    }

    private Integer getExpRandom(Integer max) {
        Double average = ((double)max + 1.0)/ 2.0;
        Double doubleValue = -average * Math.log(random.nextDouble());
        Integer value = (int)Math.round(Math.floor(doubleValue));
        if (value > max)
            value = 0;

        return value;
    }

    public String toString() {
        return String.format("IntValueRandom[distribution: %s, min: %d, max: %d]", distributionType, min, max);
    }
}