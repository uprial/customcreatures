package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

public class HItem {
    public static HItem getFromConfig(FileConfiguration config, CustomLogger customLogger, String key) throws InvalidConfigException {
        HItemFilter formula = HItemFilter.getFromConfig(config, customLogger, key + ".filter", key);
        if (null == formula)
            return null;
        return null;
    }
}
