package com.gmail.uprial.customcreatures.schema.numerics;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import static com.gmail.uprial.customcreatures.config.ConfigReaderNumbers.getDouble;
import static com.gmail.uprial.customcreatures.config.ConfigReaderNumbers.getInt;

public final class ValueSimple<T> implements IValue<T> {
    private final T value;

    private ValueSimple(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    public static boolean is(FileConfiguration config, String key) {
        return config.isInt(key) || config.isDouble(key);
    }

    public static ValueSimple<Integer> getIntFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title,
                                                        int hardMin, int hardMax) throws InvalidConfigException {
        return new ValueSimple<>(getInt(config, customLogger, key, title, hardMin, hardMax));
    }

    public static ValueSimple<Double> getDoubleFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title,
                                                          double hardMin, double hardMax) throws InvalidConfigException {
        return new ValueSimple<>(getDouble(config, customLogger, key, title, hardMin, hardMax));
    }

    public String toString() {
        return value.toString();
    }
}
