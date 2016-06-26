package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

public class HValue {

    public static IDoubleValue getDoubleFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title, String handlerName) throws InvalidConfigException {
        if (null == config.get(key)) {
            customLogger.debug(String.format("Empty %s '%s'. Use default value NULL", title, handlerName));
            return null;
        }

        if (DoubleValueSimple.is(config, key)) {
            return DoubleValueSimple.getFromConfig(config, key);
        } else if (DoubleValueRandom.is(config, key)) {
            return DoubleValueRandom.getFromConfig(config, customLogger, key, title, handlerName);
        } else
            throw new InvalidConfigException(String.format("Wrong type of %s '%s'", title, handlerName));
    }

    public static IIntValue getIntFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title, String handlerName) throws InvalidConfigException {
        if (null == config.get(key)) {
            customLogger.debug(String.format("Empty %s '%s'. Use default value NULL", title, handlerName));
            return null;
        }

        if (IntValueSimple.is(config, key)) {
            return IntValueSimple.getFromConfig(config, key);
        } else if (IntValueRandom.is(config, key)) {
            return IntValueRandom.getFromConfig(config, customLogger, key, title, handlerName);
        } else
            throw new InvalidConfigException(String.format("Wrong type of %s '%s'", title, handlerName));
    }
}