package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.enums.TrimMaterialEnum;
import com.gmail.uprial.customcreatures.schema.enums.TrimPatternEnum;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getEnum;

public final class HItemArmorTrim {

    private final String title;
    private final TrimMaterialEnum material;
    private final TrimPatternEnum pattern;

    private HItemArmorTrim(String title, TrimMaterialEnum material, TrimPatternEnum pattern) {
        this.title = title;
        this.material = material;
        this.pattern = pattern;
    }

    public void apply(CustomLogger customLogger, Entity entity, ItemStack itemStack) {
        final ArmorMeta armorMeta = (ArmorMeta)itemStack.getItemMeta();

        if(armorMeta != null) {
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s: set trim of %s to %s-%s",
                        title, format(entity), material, pattern));
            }
            armorMeta.setTrim(new ArmorTrim(material.getType(), pattern.getType()));
            itemStack.setItemMeta(armorMeta);
        }
    }

    public static HItemArmorTrim getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        final TrimMaterialEnum material = getEnum(TrimMaterialEnum.class, config,
                joinPaths(key, "material"), String.format("material of %s", title));
        final TrimPatternEnum pattern = getEnum(TrimPatternEnum.class, config,
                joinPaths(key, "pattern"), String.format("pattern of %s", title));

        return new HItemArmorTrim(title, material, pattern);
    }

    public String toString() {
        return String.format("Trim{material: %s, pattern: %s}", material, pattern);
    }
}
