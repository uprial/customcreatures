package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigReaderSimple {
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

}
