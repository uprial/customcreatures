package com.gmail.uprial.customcreatures.schema;

import org.bukkit.configuration.file.FileConfiguration;

abstract public class AbstractValueSimple {
    public static boolean is(FileConfiguration config, String key) {
        return config.isInt(key) || config.isDouble(key);
    }
}
