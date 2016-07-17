package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigReaderEnums {
    public static <T extends Enum> Set<T> getSet(Class<T> enumType, FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        List<String> strings = getStringList(config, customLogger, key, title);
        if (null == strings)
            return null;

        Set<T> set = new HashSet<>();
        for(int i = 0; i < strings.size(); i++) {
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

    protected static <T extends Enum> T getEnumFromString(Class<T> enumType, String string, String title, String desc) throws InvalidConfigException {
        try {
            //noinspection unchecked,RedundantCast
            return (T)Enum.valueOf(enumType, string.toUpperCase());
        } catch (java.lang.IllegalArgumentException e) {
            throw new InvalidConfigException(String.format("Invalid %s '%s' in %s%s", enumType.getName(), string, title, desc));
        }
    }

    protected static String getString(FileConfiguration config, String key, String title) throws InvalidConfigException {
        String string = config.getString(key);

        if(null == string) {
            throw new InvalidConfigException(String.format("Null or empty %s", title));
        }

        return string;
    }

    protected static List<String> getStringList(FileConfiguration config, CustomLogger customLogger, String key, String title) {
        List<?> lines = config.getList(key);
        if(null != lines) {
            List<String> strings = new ArrayList<>();
            for(int i = 0; i < lines.size(); i++)
                strings.add(lines.get(i).toString());

            return strings;
        } else {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }
    }
}
