package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gmail.uprial.customcreatures.common.Utils.getParentPath;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;

public class HItemEffectsList {

    private final List<HItemEffect> itemEffects;

    private HItemEffectsList(List<HItemEffect> itemEffects) {
        this.itemEffects = itemEffects;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity) {
        for (HItemEffect itemEffect : itemEffects) {
            itemEffect.apply(customLogger, entity);
        }
    }

    public static HItemEffectsList getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        List<?> itemEffectsConfig = config.getList(key);
        if((null == itemEffectsConfig) || (itemEffectsConfig.size() <= 0)) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        Map<String,Integer> keys = new HashMap<>();
        List<HItemEffect> itemEffects = new ArrayList<>();

        for(int i = 0; i < itemEffectsConfig.size(); i++) {
            Object item = itemEffectsConfig.get(i);
            if (null == item) {
                throw new InvalidConfigException(String.format("Null key in %s at pos %d", title, i));
            }
            if (! (item instanceof String)) {
                throw new InvalidConfigException(String.format("Key '%s' in %s at pos %d is not a string", item.toString(), title, i));
            }
            String subKey = item.toString();
            if (subKey.length() < 1) {
                throw new InvalidConfigException(String.format("Empty key in %s at pos %d", title, i));
            }
            String subKeyFull;
            if (null != config.get(subKey)) {
                subKeyFull = subKey;
            } else {
                String relativeKeyFull = joinPaths(getParentPath(key), subKey);
                if (! relativeKeyFull.equals(subKey)) {
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

            itemEffects.add(HItemEffect.getFromConfig(config, customLogger, subKeyFull, String.format("effect '%s' in %s", subKeyFull, title)));
        }

        return new HItemEffectsList(itemEffects);
    }

    public String toString() {
        return String.format("{%s}", joinStrings(", ", itemEffects));
    }
}