package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

import static com.gmail.uprial.customcreatures.config.ConfigUtils.getParentPath;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;

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
        return set;
    }

    public static <T extends Enum> T getEnum(Class<T> enumType, FileConfiguration config, String key, String title) throws InvalidConfigException {
        String string = getString(config, key, title);
        return getEnumFromString(enumType, string, title, "");
    }

    public static <T extends Enum> T getEnumFromString(Class<T> enumType, String string, String title, String desc) throws InvalidConfigException {
        try {
            //noinspection unchecked,RedundantCast
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

    public static String getKey(Object item, String title, int i) throws InvalidConfigException {
        if (null == item) {
            throw new InvalidConfigException(String.format("Null key in %s at pos %d", title, i));
        }
        if (!(item instanceof String)) {
            throw new InvalidConfigException(String.format("Key '%s' in %s at pos %d is not a string", item.toString(), title, i));
        }
        String key = item.toString();
        if (key.length() < 1) {
            throw new InvalidConfigException(String.format("Empty key in %s at pos %d", title, i));
        }
        return key;
    }

    public static Set<String> getItemsList(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        List<?> itemsConfig = config.getList(key);
        if((null == itemsConfig) || (itemsConfig.size() <= 0)) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        Map<String,Integer> keys = new HashMap<>();

        for(int i = 0; i < itemsConfig.size(); i++) {
            String subKey = getKey(itemsConfig.get(i), title, i);
            String subKeyFull;
            if (null != config.get(subKey)) {
                subKeyFull = subKey;
            } else {
                String relativeKeyFull = joinPaths(getParentPath(key), subKey);
                if (!relativeKeyFull.equals(subKey)) {
                    if (null != config.get(relativeKeyFull)) {
                        subKeyFull = relativeKeyFull;
                    } else {
                        throw new InvalidConfigException(String.format("Null definition of keys '%s' and '%s' in %s at pos %d",
                                relativeKeyFull, subKey, title, i));
                    }
                } else {
                    throw new InvalidConfigException(String.format("Null definition of key '%s' in %s at pos %d",
                            subKey, title, i));
                }
            }
            if (keys.containsKey(subKeyFull.toLowerCase())) {
                throw new InvalidConfigException(String.format("Key '%s' in %s is not unique", subKey, title));
            }

            keys.put(subKeyFull.toLowerCase(), 1);
        }

        return keys.keySet();
    }

}
