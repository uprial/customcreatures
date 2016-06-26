package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

import static com.gmail.uprial.customcreatures.config.ConfigReader.getInt;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.*;

public class IntValueRandom extends AbstractValueRandom implements IIntValue {
    private static final RandomDistributionType defaultDistributionType = NORMAL;

    protected final RandomDistributionType distributionType;
    protected final int min;
    protected final int max;
    private final Random random = new Random();

    IntValueRandom(RandomDistributionType distributionType, int min, int max) {
        this.distributionType = distributionType;
        this.min = min;
        this.max = max;
    }

    @Override
    public int getValue() {
        if (distributionType == EXP_DOWN) {
            return getIntRandom(max - min) + min;
        } else if (distributionType == EXP_UP) {
            return max - getIntRandom(max - min);
        } else
            return random.nextInt(max - min + 1) + min;
    }

    public static IntValueRandom getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title, String handlerName) throws InvalidConfigException {
        RandomDistributionType distributionType = getDistributionTypeFromConfig(config, customLogger, key, title, handlerName);
        int min = getInt(config, key + ".min", String.format("minimum of %s", title), handlerName);
        int max = getInt(config, key + ".max", String.format("maximum of %s", title), handlerName);

        return new IntValueRandom(distributionType, min, max);
    }

    private int getIntRandom(int max) {
        double average = ((double)max + 1.0)/ 2.0;
        double doubleValue = -average * Math.log(random.nextDouble());
        int value = (int)Math.round(Math.floor(doubleValue));
        if (value > max)
            value = 0;

        return value;
    }
}