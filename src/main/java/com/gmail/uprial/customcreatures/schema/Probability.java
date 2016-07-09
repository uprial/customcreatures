package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

import static com.gmail.uprial.customcreatures.config.ConfigReader.getInt;

public class Probability {
    public static final int MAX_PERCENT = 100;

    private final int probability;
    private final Random random = new Random();

    protected Probability(int probability) {
        this.probability = probability;
    }

    public boolean pass() {
        return (MAX_PERCENT <= probability) || (random.nextInt(MAX_PERCENT) < probability);
    }

    public static Probability getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        int probability = getInt(config, customLogger, key, title, 0, MAX_PERCENT, MAX_PERCENT);
        if (MAX_PERCENT <= probability) {
            return null;
        }

        return new Probability(probability);
    }

    public String toString() {
        return String.valueOf(probability);
    }
}
