package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReader.getEnum;
import static com.gmail.uprial.customcreatures.config.ConfigReader.getInt;
import static com.gmail.uprial.customcreatures.schema.BodyType.*;
import static com.gmail.uprial.customcreatures.schema.Probability.MAX_PERCENT;

public class HItemEquipmentCloth {
    private final String title;
    private final Probability probability;
    private final MaterialType material;
    private final BodyType bodyType;
    private final HItemEnchantmentsList enchantments;
    private final int dropChance;

    private HItemEquipmentCloth(String title, Probability probability, MaterialType material, BodyType bodyType, HItemEnchantmentsList enchantments, int dropChance) {
        this.title = title;
        this.probability = probability;
        this.material = material;
        this.bodyType = bodyType;
        this.enchantments = enchantments;
        this.dropChance = dropChance;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity) {
        if ((null == probability) || (probability.pass())) {
            Material itemMaterial;
            try {
                itemMaterial = getItemMaterial(material, bodyType, title);
            } catch (InvalidConfigException e) {
                customLogger.error(e.getMessage());
                return;
            }
            ItemStack itemStack = new ItemStack(itemMaterial);
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s: add %s to %s",
                        title, itemMaterial, format(entity)));
            }
            if (null != enchantments) {
                enchantments.apply(customLogger, entity, itemStack);
            }
            EntityEquipment entityEquipment = entity.getEquipment();

            setBodyItem(entityEquipment, itemStack);
            if (dropChance > 0) {
                if (customLogger.isDebugMode()) {
                    customLogger.debug(String.format("Handle %s: set drop chance of %s of %s to %d",
                            title, itemMaterial, format(entity), dropChance));
                }
                setBodyItemDropChance(entityEquipment, dropChance);
            }
        }
    }

    private void setBodyItem(EntityEquipment entityEquipment, ItemStack itemStack) {
        if (bodyType == HELMET) {
            entityEquipment.setHelmet(itemStack);
        } else if (bodyType == BOOTS) {
            entityEquipment.setBoots(itemStack);
        } else if (bodyType == CHESTPLATE) {
            entityEquipment.setChestplate(itemStack);
        } else if (bodyType == LEGGINGS) {
            entityEquipment.setLeggings(itemStack);
        }
    }

    private void setBodyItemDropChance(EntityEquipment entityEquipment, int dropChance) {
        if (bodyType == HELMET) {
            entityEquipment.setHelmetDropChance(dropChance);
        } else if (bodyType == BOOTS) {
            entityEquipment.setBootsDropChance(dropChance);
        } else if (bodyType == CHESTPLATE) {
            entityEquipment.setChestplateDropChance(dropChance);
        } else if (bodyType == LEGGINGS) {
            entityEquipment.setLeggingsDropChance(dropChance);
        }
    }

    public static HItemEquipmentCloth getFromConfig(FileConfiguration config, CustomLogger customLogger, BodyType bodyType, String key, String title) throws InvalidConfigException {
        if (null == config.get(key)) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        Probability probability = Probability.getFromConfig(config, customLogger, joinPaths(key, "probability"),
                String.format("probability of %s", title));

        MaterialType material = getEnum(MaterialType.class, config,
                joinPaths(key, "material"), String.format("material of %s", title));
        getItemMaterial(material, bodyType, title);

        int dropChance = getInt(config, customLogger, joinPaths(key, "drop-chance"),
                String.format("drop chance of %s", title), 0, MAX_PERCENT, 0);
        HItemEnchantmentsList enchantments = HItemEnchantmentsList.getFromConfig(config, customLogger,
                joinPaths(key, "enchantments"), String.format("enchantments of %s", title));

        return new HItemEquipmentCloth(title, probability, material, bodyType, enchantments, dropChance);
    }

    private static Material getItemMaterial(MaterialType material, BodyType bodyType, String title) throws InvalidConfigException {
        String itemName = String.format("%s_%s", material, bodyType);
        try {
            return Material.valueOf(itemName);
        } catch (java.lang.IllegalArgumentException e) {
            throw new InvalidConfigException(String.format("Invalid item material '%s' of %s", itemName, title));
        }
    }

    public String toString() {
        return String.format("[probability: %s, material: %s, enchantments: %s, drop-chance: %d]",
                probability, material, enchantments, dropChance);
    }

}