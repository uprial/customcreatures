package com.gmail.uprial.customcreatures.schema.numerics;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import static com.gmail.uprial.customcreatures.config.ConfigReaderSimple.getDouble;
import static com.gmail.uprial.customcreatures.config.ConfigReaderSimple.getInt;

public class ValueSimple<T> implements IValue<T> {
    private final T value;

    private ValueSimple(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public static boolean is(FileConfiguration config, String key) {
        return config.isInt(key) || config.isDouble(key);
    }

    public static ValueSimple<Integer> getIntFromConfig(FileConfiguration config, String key, String title,
                                                        int hardMin, int hardMax) throws InvalidConfigException {
        return new ValueSimple<>(getInt(config, key, title, hardMin, hardMax));
    }

    public static ValueSimple<Double> getDoubleFromConfig(FileConfiguration config, String key, String title,
                                                          double hardMin, double hardMax) throws InvalidConfigException {
        return new ValueSimple<>(getDouble(config, key, title, hardMin, hardMax));
    }

    public String toString() {
        return getValue().toString();
    }
}
