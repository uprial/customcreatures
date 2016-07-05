package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigReader {
    public static boolean getBoolean(FileConfiguration config, CustomLogger customLogger, String key, String title, boolean defaultValue) throws InvalidConfigException {
        boolean value = defaultValue;

        if(null == config.getString(key)) {
            customLogger.debug(String.format("Empty %s. Use default value %b", title, defaultValue));
        } else {
            String strValue = config.getString(key);
            if(strValue.equalsIgnoreCase("true"))
                value = true;
            else if(strValue.equalsIgnoreCase("false"))
                value = false;
            else
                throw new InvalidConfigException(String.format("Invalid %s. Use default value %b", title, defaultValue));
        }

        return value;
    }

    public static List<String> getStringList(FileConfiguration config, CustomLogger customLogger, String key, String title) {
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

    public static int getInt(FileConfiguration config, CustomLogger customLogger, String key, String title, int min, int max, int defaultValue) throws InvalidConfigException {
        int value = defaultValue;

        if(null == config.getString(key)) {
            customLogger.debug(String.format("Empty %s. Use default value %d", title, defaultValue));
        } else if (! config.isInt(key)) {
            throw new InvalidConfigException(String.format("A %s is not an integer", title));
        } else {
            int intValue = config.getInt(key);
            if(min > intValue)
                throw new InvalidConfigException(String.format("A %s should be at least %d. Use default value %d", title, min, defaultValue));
            else if(max < intValue)
                throw new InvalidConfigException(String.format("A %s should be at most %d. Use default value %d", title, max, defaultValue));
            else
                value = intValue;
        }

        return value;
    }

    public static int getInt(FileConfiguration config, String key, String title) throws InvalidConfigException {
        if(null == config.getString(key)) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        } else if (! config.isInt(key)) {
            throw new InvalidConfigException(String.format("A %s is not an integer", title));
        } else {
            return config.getInt(key);
        }
    }

    public static double getDouble(FileConfiguration config, String key, String title) throws InvalidConfigException {
        if(null == config.getString(key)) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        } else if ((! config.isDouble(key)) && (! config.isInt(key))) {
            throw new InvalidConfigException(String.format("A %s is not a double", title));
        } else {
            return config.getDouble(key);
        }
    }

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
        return set.size() > 0 ? set : null;
    }

    public static <T extends Enum> T getEnum(Class<T> enumType, FileConfiguration config, String key, String title) throws InvalidConfigException {
        String string = getString(config, key, title);
        return getEnumFromString(enumType, string, title, "");
    }

    public static <T extends Enum> T getEnumFromString(Class<T> enumType, String string, String title, String desc) throws InvalidConfigException {
        try {
            //noinspection unchecked
            return (T)Enum.valueOf(enumType, string.toUpperCase());
        } catch (java.lang.IllegalArgumentException e) {
            throw new InvalidConfigException(String.format("Invalid %s '%s' in %s%s", enumType.getName(), string, title, desc));
        }
    }

    public static String getString(FileConfiguration config, String key, String title) throws InvalidConfigException {
        String string = config.getString(key);

        if(null == string) {
            throw new InvalidConfigException(String.format("Null or empty %s", title));
        }

        return string;
    }
}
