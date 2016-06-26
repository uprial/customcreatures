package com.gmail.uprial.customcreatures.schema;

import org.bukkit.configuration.file.FileConfiguration;

public class DoubleValueSimple extends AbstractValueSimple implements IDoubleValue {

    private final double value;

    private DoubleValueSimple(double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        return value;
    }

    public static DoubleValueSimple getFromConfig(FileConfiguration config, String key) {
        return new DoubleValueSimple(config.getDouble(key));
    }
}