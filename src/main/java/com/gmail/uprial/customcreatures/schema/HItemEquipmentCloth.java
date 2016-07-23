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
import static com.gmail.uprial.customcreatures.config.ConfigReaderSimple.getInt;
import static com.gmail.uprial.customcreatures.schema.EntityEquipmentHelper.setItem;
import static com.gmail.uprial.customcreatures.schema.EntityEquipmentHelper.setItemDropChance;
import static com.gmail.uprial.customcreatures.schema.Probability.MAX_PERCENT;

public final class HItemEquipmentCloth {
    private final String title;
    private final Probability probability;
    private final MaterialType materialType;
    private final ClothType clothType;
    private final HItemEnchantmentsList enchantments;
    private final int dropChance;
    private final HItemDurability durability;

    private HItemEquipmentCloth(String title, Probability probability, MaterialType materialType,
                                ClothType clothType, HItemEnchantmentsList enchantments, int dropChance, HItemDurability durability) {
        this.title = title;
        this.probability = probability;
        this.materialType = materialType;
        this.clothType = clothType;
        this.enchantments = enchantments;
        this.dropChance = dropChance;
        this.durability = durability;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity) {
        if ((probability == null) || (probability.isPassed())) {
            Material material;
            try {
                material = getMaterial(materialType, clothType, title);
            } catch (InvalidConfigException e) {
                customLogger.error(e.getMessage());
                return;
            }

            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s: add %s to %s",
                        title, material, format(entity)));
            }
            ItemStack itemStack = new ItemStack(material);

            if (enchantments != null) {
                enchantments.apply(customLogger, entity, itemStack);
            }

            if (durability != null) {
                durability.apply(customLogger, entity, itemStack);
            }

            try {
                setItem(entity.getEquipment(), clothType, itemStack);
            } catch (OperationIsNotSupportedException e) {
                customLogger.error(String.format("Can't handle %s: %s", title, e.getMessage()));
                return ;
            }

            if (dropChance > 0) {
                if (customLogger.isDebugMode()) {
                    customLogger.debug(String.format("Handle drop chance of %s: set drop chance of %s of %s to %d",
                            title, material, format(entity), dropChance));
                }
                try {
                    setItemDropChance(entity.getEquipment(), clothType, dropChance);
                } catch (OperationIsNotSupportedException e) {
                    customLogger.error(String.format("Can't handle drop chance of %s: %s", title, e.getMessage()));
                    //noinspection UnnecessaryReturnStatement
                    return ;
                }
            }
        }
    }

    public static HItemEquipmentCloth getFromConfig(FileConfiguration config, CustomLogger customLogger, ClothType clothType, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        Probability probability = Probability.getFromConfig(config, customLogger, joinPaths(key, "probability"),
                String.format("probability of %s", title));

        MaterialType materialType = getEnum(MaterialType.class, config,
                joinPaths(key, "material-type"), String.format("material type of %s", title));
        getMaterial(materialType, clothType, title);

        int dropChance = getInt(config, customLogger, joinPaths(key, "drop-chance"),
                String.format("drop chance of %s", title), 0, MAX_PERCENT, 0);
        HItemDurability durability = HItemDurability.getFromConfig(config, customLogger, joinPaths(key, "durability"),
                String.format("durability of %s", title));
        HItemEnchantmentsList enchantments = HItemEnchantmentsList.getFromConfig(config, customLogger,
                joinPaths(key, "enchantments"), String.format("enchantments of %s", title));

        return new HItemEquipmentCloth(title, probability, materialType, clothType, enchantments, dropChance, durability);
    }

    private static Material getMaterial(MaterialType materialType, ClothType clothType, String title) throws InvalidConfigException {
        String itemName = String.format("%s_%s", materialType, clothType);
        try {
            return Material.valueOf(itemName);
        } catch (IllegalArgumentException ignored) {
            throw new InvalidConfigException(String.format("Invalid item material type '%s' of %s", itemName, title));
        }
    }

    public String toString() {
        return String.format("[probability: %s, material-type: %s, enchantments: %s, drop-chance: %d, durability: %s]",
                probability, materialType, enchantments, dropChance, durability);
    }

}