package com.gmail.uprial.customcreatures.schema;

import org.bukkit.configuration.file.FileConfiguration;

public class ValueSimple implements IValue {

    private final double value;

    private ValueSimple(double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        return value;
    }

    public static boolean is(FileConfiguration config, String key) {
        return config.isInt(key) || config.isDouble(key);
    }

    public static ValueSimple getFromConfig(FileConfiguration config, String key) {
        return new ValueSimple(config.getDouble(key));
    }
}