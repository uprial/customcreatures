package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

import static com.gmail.uprial.customcreatures.config.ConfigReaderSimple.getString;
import static com.gmail.uprial.customcreatures.config.ConfigReaderSimple.getStringList;

public final class ConfigReaderEnums {
    public static <T extends Enum> Set<T> getSet(Class<T> enumType, FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        List<String> strings = getStringList(config, customLogger, key, title);
        if (strings == null) {
            return null;
        }

        Set<T> set = new HashSet<>();
        int stringSize = strings.size();
        for(int i = 0; i < stringSize; i++) {
            String string = strings.get(i);
            T enumItem = getEnumFromString(enumType, string, title, String.format(" at pos %d", i));
            if (set.contains(enumItem)) {
                throw new InvalidConfigException(String.format("%s '%s' in %s is not unique", enumType.getName(), string, title));
            }
            set.add(enumItem);
        }
        return set;
    }

    public static <T extends Enum> T getEnum(Class<T> enumType, FileConfiguration config, String key, String title) throws InvalidConfigException {
        String string = getString(config, key, title);
        return getEnumFromString(enumType, string, title, "");
    }

    static <T extends Enum> T getEnumFromString(Class<T> enumType, String string, String title, String desc) throws InvalidConfigException {
        try {
            //noinspection unchecked,RedundantCast
            return (T)Enum.valueOf(enumType, string.toUpperCase(Locale.getDefault()));
        } catch (IllegalArgumentException ignored) {
            throw new InvalidConfigException(String.format("Invalid %s '%s' in %s%s", enumType.getName(), string, title, desc));
        }
    }
}
