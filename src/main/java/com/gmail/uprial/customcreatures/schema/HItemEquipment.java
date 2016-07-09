package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.schema.BodyType.HELMET;

public class HItemEquipment {
    private final HItemEquipmentCloth helmet;

    private HItemEquipment(HItemEquipmentCloth helmet) {
        this.helmet = helmet;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity) {
        if (null != helmet) {
            helmet.apply(customLogger, entity);
        }
    }

    public static HItemEquipment getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (null == config.get(key)) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        HItemEquipmentCloth helmet = HItemEquipmentCloth.getFromConfig(config, customLogger, HELMET,
                joinPaths(key, "helmet"), String.format("helmet of %s", title));
        if ((null == helmet)) {
            throw new InvalidConfigException(String.format("No cloths found in %s", title));
        }

        return new HItemEquipment(helmet);
    }

    public String toString() {
        return String.format("[helmet: %s]",
                helmet);
    }
}
