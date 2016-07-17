package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;
import static com.gmail.uprial.customcreatures.config.ConfigReaderLists.getItemsList;

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
        Set<String> subKeys = getItemsList(config, customLogger, key, title);
        if (null == subKeys) {
            return null;
        }

        List<HItemEffect> itemEffects = new ArrayList<>();

        for(String subKey : subKeys) {
            itemEffects.add(HItemEffect.getFromConfig(config, customLogger, subKey, String.format("effect '%s' in %s", subKey, title)));
        }

        return new HItemEffectsList(itemEffects);
    }

    public String toString() {
        return String.format("{%s}", joinStrings(", ", itemEffects));
    }
}