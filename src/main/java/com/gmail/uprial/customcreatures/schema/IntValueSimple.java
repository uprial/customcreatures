package com.gmail.uprial.customcreatures.schema;

import org.bukkit.configuration.file.FileConfiguration;

public class IntValueSimple extends AbstractValueSimple<Integer> {

    private IntValueSimple(Integer value) {
        super(value);
    }

    public static IntValueSimple getFromConfig(FileConfiguration config, String key) {
        return new IntValueSimple(config.getInt(key));
    }
}
