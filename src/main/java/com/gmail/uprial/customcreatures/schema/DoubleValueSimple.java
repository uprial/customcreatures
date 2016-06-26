package com.gmail.uprial.customcreatures.schema;

import org.bukkit.configuration.file.FileConfiguration;

public class DoubleValueSimple extends AbstractValueSimple<Double> {

    private DoubleValueSimple(Double value) {
        super(value);
    }

    public static DoubleValueSimple getFromConfig(FileConfiguration config, String key) {
        return new DoubleValueSimple(config.getDouble(key));
    }
}