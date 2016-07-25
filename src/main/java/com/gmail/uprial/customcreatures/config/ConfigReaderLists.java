package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigUtils.getParentPath;

public final class ConfigReaderLists {
    public static String getKey(Object item, String title, int i) throws InvalidConfigException {
        if (item == null) {
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
        if((itemsConfig == null) || (itemsConfig.size() <= 0)) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        Map<String,Integer> keys = new HashMap<>();

        int itemsConfigSize = itemsConfig.size();
        for(int i = 0; i < itemsConfigSize; i++) {
            String subKey = getKey(itemsConfig.get(i), title, i);
            String subKeyFull;
            if (config.get(subKey) != null) {
                subKeyFull = subKey;
            } else {
                String relativeKeyFull = joinPaths(getParentPath(key), subKey);
                if (relativeKeyFull.equals(subKey)) {
                    throw new InvalidConfigException(String.format("Null definition of key '%s' in %s at pos %d",
                            subKey, title, i));
                } else {
                    if (config.get(relativeKeyFull) != null) {
                        subKeyFull = relativeKeyFull;
                    } else {
                        throw new InvalidConfigException(String.format("Null definition of keys '%s' and '%s' in %s at pos %d",
                                relativeKeyFull, subKey, title, i));
                    }
                }
            }
            String subKeyFullLC = subKeyFull.toLowerCase(Locale.getDefault());
            if (keys.containsKey(subKeyFullLC)) {
                throw new InvalidConfigException(String.format("Key '%s' in %s is not unique", subKey, title));
            }

            keys.put(subKeyFullLC, 1);
        }

        return keys.keySet();
    }


}
