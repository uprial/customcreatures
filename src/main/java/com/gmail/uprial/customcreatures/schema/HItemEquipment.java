package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.google.common.collect.ImmutableMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;
import static com.gmail.uprial.customcreatures.schema.BodyType.*;
import static com.gmail.uprial.customcreatures.schema.HandType.MAIN_HAND;
import static com.gmail.uprial.customcreatures.schema.HandType.OFF_HAND;

public class HItemEquipment {
    private static final Map<String, BodyType> key2bodyType = ImmutableMap.<String, BodyType>builder()
            .put("helmet", HELMET)
            .put("boots", BOOTS)
            .put("chest", CHESTPLATE)
            .put("leggings", LEGGINGS)
            .build();
    private static final Map<String, HandType> key2handType = ImmutableMap.<String, HandType>builder()
            .put("main-hand", MAIN_HAND)
            .put("off-hand", OFF_HAND)
            .build();

    private final Map<String,HItemEquipmentCloth> cloths;
    private final Map<String,HItemInHand> tools;

    private HItemEquipment(Map<String,HItemEquipmentCloth> cloths, Map<String,HItemInHand> tools) {
        this.cloths = cloths;
        this.tools = tools;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity) {
        for (HItemEquipmentCloth cloth : cloths.values()) {
            cloth.apply(customLogger, entity);
        }
        for (HItemInHand tool : tools.values()) {
            tool.apply(customLogger, entity);
        }
    }

    public static HItemEquipment getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (null == config.get(key)) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        Map<String,HItemEquipmentCloth> cloths = new HashMap<>();
        for (Map.Entry<String,BodyType> entry : key2bodyType.entrySet()) {
            HItemEquipmentCloth cloth = HItemEquipmentCloth.getFromConfig(config, customLogger, entry.getValue(),
                    joinPaths(key, entry.getKey()), String.format("%s of %s", entry.getKey(), title));
            if (null != cloth) {
                cloths.put(entry.getKey(), cloth);
            }
        }
        Map<String,HItemInHand> tools = new HashMap<>();
        for (Map.Entry<String,HandType> entry : key2handType.entrySet()) {
            HItemInHand tool = HItemInHand.getFromConfig(config, customLogger, entry.getValue(),
                    joinPaths(key, entry.getKey()), String.format("%s of %s", entry.getKey(), title));
            if (null != tool) {
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
        for (String key : key2bodyType.keySet()) {
            items.add(String.format("%s: %s", key, cloths.get(key)));
        }
        for (String key : key2handType.keySet()) {
            items.add(String.format("%s: %s", key, tools.get(key)));
        }
        return String.format("[%s]", joinStrings(", ", items));
    }
}
