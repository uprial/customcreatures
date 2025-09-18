package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.exceptions.OperationIsNotSupportedException;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getEnum;
import static com.gmail.uprial.customcreatures.schema.EntityEquipmentHelper.*;

public final class HItemEquipmentSpecial {
    private final String title;
    private final Probability probability;
    private final Material material;
    private final EquipmentType equipmentType;
    private final HItemEnchantmentsList enchantments;
    private final HItemDurability durability;

    private HItemEquipmentSpecial(String title, Probability probability, Material material, EquipmentType equipmentType,
                                  HItemEnchantmentsList enchantments, HItemDurability durability) {
        this.title = title;
        this.probability = probability;
        this.material = material;
        this.equipmentType = equipmentType;
        this.enchantments = enchantments;
        this.durability = durability;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity) {
        if ((probability == null) || (probability.isPassed())) {
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s: add %s to %s",
                        title, material, format(entity)));
            }
            final ItemStack itemStack = new ItemStack(material);

            if (enchantments != null) {
                enchantments.apply(customLogger, entity, itemStack);
            }

            if (durability != null) {
                durability.apply(customLogger, entity, itemStack);
            }

            try {
                setItem(entity, equipmentType, itemStack);
            } catch (OperationIsNotSupportedException e) {
                customLogger.error(String.format("Can't handle %s: %s", title, e.getMessage()));
            }
        }
    }

    public static HItemEquipmentSpecial getFromConfig(FileConfiguration config, CustomLogger customLogger, EquipmentType equipmentType, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        final Probability probability = Probability.getFromConfig(config, customLogger, joinPaths(key, "probability"),
                String.format("probability of %s", title));

        final Material material = getEnum(Material.class, config,
                joinPaths(key, "material"), String.format("material of %s", title));
        final HItemDurability durability = HItemDurability.getFromConfig(config, customLogger, joinPaths(key, "durability"),
                String.format("durability of %s", title));
        final HItemEnchantmentsList enchantments = HItemEnchantmentsList.getFromConfig(config, customLogger,
                joinPaths(key, "enchantments"), String.format("enchantments of %s", title));

        return new HItemEquipmentSpecial(title, probability, material, equipmentType, enchantments, durability);
    }

    public String toString() {
        return String.format("{probability: %s, material: %s, enchantments: %s, durability: %s}",
                probability, material, enchantments, durability);
    }

}
