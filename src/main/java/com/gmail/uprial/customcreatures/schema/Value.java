package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

abstract public class Value {

    abstract public int getValue();

    public static Value getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title, String handlerName) throws InvalidConfigException {
        if (ValueSimple.is(config, key)) {
            return ValueSimple.getFromConfig(config, key);
        } else if (ValueRandom.is(config, key)) {
            return ValueRandom.getFromConfig(config, customLogger, key, title, handlerName);
        } else
            throw new InvalidConfigException(String.format("Wrong type of %s '%s'", title, handlerName));
    }
}