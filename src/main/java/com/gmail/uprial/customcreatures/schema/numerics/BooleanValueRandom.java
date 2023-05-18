package com.gmail.uprial.customcreatures.schema.numerics;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderNumbers.getInt;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.EXP_DOWN;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.EXP_UP;

public class BooleanValueRandom extends AbstractValueRandom<Boolean> {
    BooleanValueRandom(RandomDistributionType distributionType) {
        super(distributionType);
    }

    @Override
    public Boolean getValue() {
        if (distributionType == EXP_DOWN) {
            return getExpRandom();
        } else if (distributionType == EXP_UP) {
            return !getExpRandom();
        } else {
            return random.nextBoolean();
        }
    }

    public static BooleanValueRandom getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        RandomDistributionType distributionType = getDistributionTypeFromConfig(config, customLogger, key, title);

        return new BooleanValueRandom(distributionType);
    }

    private Boolean getExpRandom() {
        double value = - Math.log(random.nextDouble());
        // For consistency with IntValueRandom
        return (value < 1.0D) || (value > 2.0D);
    }

    public String toString() {
        return String.format("BooleanValueRandom{distribution: %s}", distributionType);
    }
}
