package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigReader {
    public static boolean getBoolean(FileConfiguration config, CustomLogger customLogger, String key, String title, String name, boolean defaultValue) throws InvalidConfigException {
        boolean value = defaultValue;

        if(null == config.getString(key)) {
            customLogger.debug(String.format("Empty %s '%s'. Use default value %b", title, name, defaultValue));
        } else {
            String strValue = config.getString(key);
            if(strValue.equalsIgnoreCase("true"))
                value = true;
            else if(strValue.equalsIgnoreCase("false"))
                value = false;
            else
                throw new InvalidConfigException(String.format("Invalid %s '%s'. Use default value %b", title, name, defaultValue));
        }

        return value;
    }

    public static List<String> getStringList(FileConfiguration config, CustomLogger customLogger, String key, String title, String name) {
		List<?> lines = config.getList(key);
		if(null != lines) {
			List<String> strings = new ArrayList<>();
			for(int i = 0; i < lines.size(); i++)
                strings.add(lines.get(i).toString());

			return strings;
		} else {
			customLogger.debug(String.format("Empty %s '%s'. Use default value NULL", title, name));
			return null;
		}
	}

    public static int getInt(FileConfiguration config, CustomLogger customLogger, String key, String title, String name, int min, int max, int defaultValue) throws InvalidConfigException {
        int value = defaultValue;

        if(null == config.getString(key)) {
            customLogger.debug(String.format("Empty %s '%s'. Use default value %d", title, name, defaultValue));
        } else if (! config.isInt(key)) {
            throw new InvalidConfigException(String.format("A %s '%s' is not an integer", title, name));
        } else {
            int intValue = config.getInt(key);
            if(min > intValue)
                throw new InvalidConfigException(String.format("A %s '%s' should be at least %d. Use default value %d", title, name, min, defaultValue));
            else if(max < intValue)
                throw new InvalidConfigException(String.format("A %s '%s' should be at most %d. Use default value %d", title, name, max, defaultValue));
            else
                value = intValue;
        }

        return value;
    }

    public static int getInt(FileConfiguration config, String key, String title, String name) throws InvalidConfigException {
        if(null == config.getString(key)) {
            throw new InvalidConfigException(String.format("Empty %s '%s'", title, name));
        } else if (! config.isInt(key)) {
            throw new InvalidConfigException(String.format("A %s '%s' is not an integer", title, name));
        } else {
            return config.getInt(key);
        }
    }

    public static <T extends Enum> Set<T> getSet(Class<T> enumType, FileConfiguration config, CustomLogger customLogger, String key, String title, String name) throws InvalidConfigException {
        List<String> strings = ConfigReader.getStringList(config, customLogger, key, title, name);
        if (null == strings)
            return null;

        Set<T> entityTypes = new HashSet<>();
        for(int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            T enumItem = getEnumFromString(enumType, string, title, name, String.format(" at pos %d", i));
            if (entityTypes.contains(enumItem)) {
                throw new InvalidConfigException(String.format("%s '%s' in %s '%s' is not unique", enumType.getName(), enumItem.toString(), title, name));
            }
            entityTypes.add(enumItem);
        }
        return entityTypes.size() > 0 ? entityTypes : null;
    }

    public static <T extends Enum> T getEnum(Class<T> enumType, FileConfiguration config, String key, String title, String name) throws InvalidConfigException {
        String string = getString(config, key, title, name);
        return getEnumFromString(enumType, string, title, name, "");
    }

    public static <T extends Enum> T getEnumFromString(Class<T> enumType, String string, String title, String name, String desc) throws InvalidConfigException {
        try {
            //noinspection unchecked
            return (T)Enum.valueOf(enumType, string.toUpperCase());
        } catch (java.lang.IllegalArgumentException e) {
            throw new InvalidConfigException(String.format("Invalid %s '%s' in %s '%s'%s", enumType.getName(), string, title, name, desc));
        }
    }

    public static String getString(FileConfiguration config, String key, String title, String name) throws InvalidConfigException {
		String string = config.getString(key);
		
		if(null == string) {
            throw new InvalidConfigException(String.format("Null or empty %s '%s'", title, name));
		}
		
		return string;
	}
}
