package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

import static com.gmail.uprial.customcreatures.common.DoubleHelper.MIN_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.common.DoubleHelper.formatDoubleValue;
import static com.gmail.uprial.customcreatures.config.ConfigReaderSimple.getDouble;

public class Probability {
    public static final int MAX_PERCENT = 100;

    private final double probability;
    private final Random random = new Random();

    Probability(double probability) {
        this.probability = probability;
    }

    public boolean isPassed() {
        return (probability >= MAX_PERCENT) || ((random.nextDouble() * MAX_PERCENT) < probability);
    }

    public static Probability getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        double probability = getDouble(config, customLogger, key, title, MIN_DOUBLE_VALUE, MAX_PERCENT, MAX_PERCENT);
        if (probability >= MAX_PERCENT) {
            return null;
        }

        return new Probability(probability);
    }

    public String toString() {
        return formatDoubleValue(probability);
    }
}
