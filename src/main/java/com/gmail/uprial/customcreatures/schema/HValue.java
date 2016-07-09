package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

public class HValue {

    public static IValue<Double> getDoubleFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (null == config.get(key)) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        if (ValueSimple.is(config, key)) {
            return ValueSimple.getDoubleFromConfig(config, key);
        } else if (DoubleValueRandom.is(config, key)) {
            return DoubleValueRandom.getFromConfig(config, customLogger, key, title);
        } else
            throw new InvalidConfigException(String.format("Wrong type of %s", title));
    }

    public static IValue<Integer> getIntFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (null == config.get(key)) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        if (ValueSimple.is(config, key)) {
            return ValueSimple.getIntFromConfig(config, key);
        } else if (IntValueRandom.is(config, key)) {
            return IntValueRandom.getFromConfig(config, customLogger, key, title);
        } else
            throw new InvalidConfigException(String.format("Wrong type of %s", title));
    }
}