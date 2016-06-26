package com.gmail.uprial.customcreatures.schema;

import org.bukkit.configuration.file.FileConfiguration;

public class IntValueSimple extends AbstractValueSimple implements IIntValue {

    private final int value;

    private IntValueSimple(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    public static IntValueSimple getFromConfig(FileConfiguration config, String key) {
        return new IntValueSimple(config.getInt(key));
    }
}
