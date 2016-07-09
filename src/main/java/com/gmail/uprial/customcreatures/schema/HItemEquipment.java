package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
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

public class HItemEquipment {
    private static final Map<String, BodyType> key2bodyType = ImmutableMap.<String, BodyType>builder()
            .put("helmet", HELMET)
            .put("boots", BOOTS)
            .put("chest", CHESTPLATE)
            .put("leggings", LEGGINGS)
            .build();

    private final Map<String,HItemEquipmentCloth> cloths;

    private HItemEquipment(Map<String,HItemEquipmentCloth> cloths) {
        this.cloths = cloths;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity) {
        for (HItemEquipmentCloth cloth : cloths.values()) {
            cloth.apply(customLogger, entity);
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
        if (cloths.size() < 1) {
            throw new InvalidConfigException(String.format("No cloths found in %s", title));
        }

        return new HItemEquipment(cloths);
    }

    public String toString() {
        List<String> items = new ArrayList<>();
        for (String key : key2bodyType.keySet()) {
            items.add(String.format("%s: %s", key, cloths.get(key)));
        }
        return String.format("[%s]", joinStrings(", ", items));
    }
}
