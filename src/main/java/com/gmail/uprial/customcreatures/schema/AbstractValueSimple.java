package com.gmail.uprial.customcreatures.schema;

import org.bukkit.configuration.file.FileConfiguration;

abstract public class AbstractValueSimple<T> implements IValue<T> {
    private final T value;

    protected AbstractValueSimple(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public static boolean is(FileConfiguration config, String key) {
        return config.isInt(key) || config.isDouble(key);
    }
}
