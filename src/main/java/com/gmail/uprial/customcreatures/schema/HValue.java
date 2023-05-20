package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.*;
import org.bukkit.configuration.file.FileConfiguration;

final class HValue {

    public static IValue<Double> getDoubleFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title,
                                                     double hardMin, double hardMax) throws InvalidConfigException {
        if (config.get(key) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        if (ValueSimple.is(config, key)) {
            return ValueSimple.getDoubleFromConfig(config, customLogger, key, title, hardMin, hardMax);
        } else if (DoubleValueRandom.is(config, key)) {
            return DoubleValueRandom.getFromConfig(config, customLogger, key, title, hardMin, hardMax);
        } else {
            throw new InvalidConfigException(String.format("Wrong type of %s", title));
        }
    }

    public static IValue<Integer> getIntFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title,
                                                   int hardMin, int hardMax) throws InvalidConfigException {
        if (config.get(key) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        if (ValueSimple.is(config, key)) {
            return ValueSimple.getIntFromConfig(config, customLogger, key, title, hardMin, hardMax);
        } else if (IntValueRandom.is(config, key)) {
            return IntValueRandom.getFromConfig(config, customLogger, key, title, hardMin, hardMax);
        } else {
            throw new InvalidConfigException(String.format("Wrong type of %s", title));
        }
    }

    public static IValue<Boolean> getBooleanFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        if (ValueSimple.is(config, key)) {
            return ValueSimple.getBooleanFromConfig(config, customLogger, key, title);
        } else if (IntValueRandom.is(config, key)) {
            return BooleanValueRandom.getFromConfig(config, customLogger, key, title);
        } else {
            throw new InvalidConfigException(String.format("Wrong type of %s", title));
        }
    }
}
