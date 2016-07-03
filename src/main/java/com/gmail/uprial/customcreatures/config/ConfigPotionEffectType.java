package com.gmail.uprial.customcreatures.config;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.gmail.uprial.customcreatures.config.ConfigReader.getStringList;

public class ConfigPotionEffectType {

    public static Set<PotionEffectType> getPotionEffectTypesSet(FileConfiguration config, CustomLogger customLogger, String key, String title, String name) throws InvalidConfigException {
        List<String> strings = getStringList(config, customLogger, key, title, name);
        if (null == strings)
            return null;

        Set<PotionEffectType> set = new HashSet<>();
        for(int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            PotionEffectType enumItem = getPotionEffectTypeFromString(string, title, name);
            if (set.contains(enumItem)) {
                throw new InvalidConfigException(String.format("PotionEffectType '%s' in %s '%s' is not unique", string, title, name));
            }
            set.add(enumItem);
        }
        return set.size() > 0 ? set : null;
    }

    public static PotionEffectType getPotionEffectTypeFromString(String string, String title, String name) throws InvalidConfigException {
        PotionEffectType potionEffectType = PotionEffectType.getByName(string);
        if (null == potionEffectType)
            throw new InvalidConfigException(String.format("Invalid PotionEffectType '%s' in %s '%s'", string, title, name));
        else
            return potionEffectType;
    }
}
