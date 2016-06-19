package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

public class ValueSimple extends Value {

    private final Integer value;

    ValueSimple(Integer value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    public static boolean is(FileConfiguration config, String key) {
        return config.isInt(key);
    }

    public static ValueSimple getFromConfig(FileConfiguration config, String key) throws InvalidConfigException {
        return new ValueSimple(config.getInt(key));
    }
}