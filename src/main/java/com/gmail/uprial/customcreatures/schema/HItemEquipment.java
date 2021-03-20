package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.google.common.collect.ImmutableMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;
import static com.gmail.uprial.customcreatures.schema.ClothType.*;
import static com.gmail.uprial.customcreatures.schema.HandType.MAIN_HAND;
import static com.gmail.uprial.customcreatures.schema.HandType.OFF_HAND;

public final class HItemEquipment {
    private static final Map<String, ClothType> KEY_2_CLOTH_TYPE = ImmutableMap.<String, ClothType>builder()
            .put("helmet", HELMET)
            .put("boots", BOOTS)
            .put("chest", CHESTPLATE)
            .put("leggings", LEGGINGS)
            .build();
    private static final Map<String, HandType> KEY_2_HAND_TYPE = ImmutableMap.<String, HandType>builder()
            .put("main-hand", MAIN_HAND)
            .put("off-hand", OFF_HAND)
            .build();

    private final Map<String,HItemEquipmentCloth> cloths;
    private final Map<String,HItemInHand> tools;

    private HItemEquipment(Map<String,HItemEquipmentCloth> cloths, Map<String,HItemInHand> tools) {
        this.cloths = cloths;
        this.tools = tools;
    }

    public void apply(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        for (HItemEquipmentCloth cloth : cloths.values()) {
            cloth.apply(plugin, customLogger, entity);
        }
        for (HItemInHand tool : tools.values()) {
            tool.apply(plugin, customLogger, entity);
        }
    }

    public static HItemEquipment getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        Map<String,HItemEquipmentCloth> cloths = new HashMap<>();
        for (Entry<String,ClothType> entry : KEY_2_CLOTH_TYPE.entrySet()) {
            HItemEquipmentCloth cloth = HItemEquipmentCloth.getFromConfig(config, customLogger, entry.getValue(),
                    joinPaths(key, entry.getKey()), String.format("%s of %s", entry.getKey(), title));
            if (cloth != null) {
                cloths.put(entry.getKey(), cloth);
            }
        }
        Map<String,HItemInHand> tools = new HashMap<>();
        for (Entry<String,HandType> entry : KEY_2_HAND_TYPE.entrySet()) {
            HItemInHand tool = HItemInHand.getFromConfig(config, customLogger, entry.getValue(),
                    joinPaths(key, entry.getKey()), String.format("%s of %s", entry.getKey(), title));
            if (tool != null) {
                tools.put(entry.getKey(), tool);
            }
        }
        if ((cloths.size() < 1) && (tools.size() < 1)) {
            throw new InvalidConfigException(String.format("No cloths or tools found in %s", title));
        }

        return new HItemEquipment(cloths, tools);
    }

    public String toString() {
        List<String> items = new ArrayList<>();
        for (String key : KEY_2_CLOTH_TYPE.keySet()) {
            items.add(String.format("%s: %s", key, cloths.get(key)));
        }
        for (String key : KEY_2_HAND_TYPE.keySet()) {
            items.add(String.format("%s: %s", key, tools.get(key)));
        }
        return String.format("[%s]", joinStrings(", ", items));
    }
}
