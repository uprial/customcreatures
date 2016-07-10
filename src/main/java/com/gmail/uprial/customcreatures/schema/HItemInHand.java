package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import com.gmail.uprial.customcreatures.schema.numerics.ValueConst;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReader.getEnum;
import static com.gmail.uprial.customcreatures.config.ConfigReader.getInt;
import static com.gmail.uprial.customcreatures.schema.EntityEquipmentHelper.setItem;
import static com.gmail.uprial.customcreatures.schema.EntityEquipmentHelper.setItemDropChance;
import static com.gmail.uprial.customcreatures.schema.Probability.MAX_PERCENT;

public class HItemInHand {
    private final String title;
    private final Probability probability;
    private final Material material;
    private final IValue<Integer> amount;
    private final HandType handType;
    private final HItemEnchantmentsList enchantments;
    private final int dropChance;
    private final HItemDurability durability;

    private HItemInHand(String title, Probability probability, Material material, IValue<Integer> amount, HandType handType,
                        HItemEnchantmentsList enchantments, int dropChance, HItemDurability durability) {
        this.title = title;
        this.probability = probability;
        this.material = material;
        this.amount = amount;
        this.handType = handType;
        this.enchantments = enchantments;
        this.dropChance = dropChance;
        this.durability = durability;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity) {
        if ((null == probability) || (probability.pass())) {
            int itemAmount = amount.getValue();
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s: add %d x %s to %s",
                        title, itemAmount, material, format(entity)));
            }
            ItemStack itemStack = new ItemStack(material, itemAmount);

            if (null != enchantments) {
                enchantments.apply(customLogger, entity, itemStack);
            }

            if (null != durability) {
                durability.apply(customLogger, entity, itemStack);
            }

            EntityEquipment entityEquipment = entity.getEquipment();

            try {
                setItem(entityEquipment, handType, itemStack);
            } catch (OffHandIsNotSupportedException e) {
                customLogger.error(String.format("Can't handle %s: %s", title, e.getMessage()));
                return ;
            }

            if (dropChance > 0) {
                if (customLogger.isDebugMode()) {
                    customLogger.debug(String.format("Handle %s: set drop chance of %s of %s to %d",
                            title, material, format(entity), dropChance));
                }
                try {
                    setItemDropChance(entityEquipment, handType, dropChance);
                } catch (OffHandIsNotSupportedException e) {
                    customLogger.error(String.format("Can't handle %s: %s", title, e.getMessage()));
                    return ;
                }
            }
        }
    }

    public static HItemInHand getFromConfig(FileConfiguration config, CustomLogger customLogger, HandType handType, String key, String title) throws InvalidConfigException {
        if (null == config.get(key)) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        Probability probability = Probability.getFromConfig(config, customLogger, joinPaths(key, "probability"),
                String.format("probability of %s", title));

        Material material = getEnum(Material.class, config,
                joinPaths(key, "material"), String.format("material of %s", title));
        IValue<Integer> amount = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "amount"),
                String.format("amount of %s", title));
        if (null == amount) {
            amount = new ValueConst<>(1);
        }

        int dropChance = getInt(config, customLogger, joinPaths(key, "drop-chance"),
                String.format("drop chance of %s", title), 0, MAX_PERCENT, 0);
        HItemDurability durability = HItemDurability.getFromConfig(config, customLogger, joinPaths(key, "durability"),
                String.format("durability of %s", title));
        HItemEnchantmentsList enchantments = HItemEnchantmentsList.getFromConfig(config, customLogger,
                joinPaths(key, "enchantments"), String.format("enchantments of %s", title));

        return new HItemInHand(title, probability, material, amount, handType, enchantments, dropChance, durability);
    }

    public String toString() {
        return String.format("[probability: %s, material: %s, amount: %s, enchantments: %s, drop-chance: %d, durability: %s]",
                probability, material, amount, enchantments, dropChance, durability);
    }

}
