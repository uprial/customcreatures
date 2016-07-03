package com.gmail.uprial.customcreatures.schema;

import org.bukkit.configuration.file.FileConfiguration;

public class ValueSimple<T> implements IValue<T> {
    private final T value;

    protected ValueSimple(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public static boolean is(FileConfiguration config, String key) {
        return config.isInt(key) || config.isDouble(key);
    }

    public static ValueSimple<Integer> getIntFromConfig(FileConfiguration config, String key) {
        return new ValueSimple<>(config.getInt(key));
    }

    public static ValueSimple<Double> getDoubleFromConfig(FileConfiguration config, String key) {
        return new ValueSimple<>(config.getDouble(key));
    }

    public String toString() {
        return getValue().toString();
    }
}
