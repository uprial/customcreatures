package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getEnum;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.NORMAL;

public final class HItemDrop {
    private final String title;
    private final Probability probability;
    private final Material material;
    private final IntValueRandom amount;
    private final HItemEnchantmentsList enchantments;
    private final HItemDurability durability;

    private HItemDrop(String title, Probability probability, Material material, IntValueRandom amount,
                      HItemEnchantmentsList enchantments, HItemDurability durability) {
        this.title = title;
        this.probability = probability;
        this.material = material;
        this.amount = amount;
        this.enchantments = enchantments;
        this.durability = durability;
    }

    public void apply(CustomLogger customLogger, EntityDeathEvent event, int lootBonusMobs) {
        if ((probability == null) || (probability.isPassed())) {
            final LivingEntity entity = event.getEntity();
            final int itemAmount = amount.getValueWithInc(0, lootBonusMobs);

            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s: add %d x %s to %s",
                        title, itemAmount, material, format(entity)));
            }
            final ItemStack itemStack = new ItemStack(material, itemAmount);

            if (enchantments != null) {
                enchantments.apply(customLogger, entity, itemStack);
            }

            if (durability != null) {
                durability.apply(customLogger, entity, itemStack);
            }

            event.getDrops().add(itemStack);
        }
    }

    public static HItemDrop getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        Probability probability = Probability.getFromConfig(config, customLogger, joinPaths(key, "probability"),
                String.format("probability of %s", title));

        Material material = getEnum(Material.class, config,
                joinPaths(key, "material"), String.format("material of %s", title));

        IValue<Integer> configuredAmount = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "amount"),
                String.format("amount of %s", title), 1, 64);

        final IntValueRandom amount;
        if (configuredAmount == null) {
            amount = new IntValueRandom(NORMAL, 1, 1);
        } else if (configuredAmount instanceof ValueSimple) {
            amount = new IntValueRandom(NORMAL, configuredAmount.getValue(), configuredAmount.getValue());
        } else {
            amount = (IntValueRandom)configuredAmount;
        }

        HItemDurability durability = HItemDurability.getFromConfig(config, customLogger, joinPaths(key, "durability"),
                String.format("durability of %s", title));
        HItemEnchantmentsList enchantments = HItemEnchantmentsList.getFromConfig(config, customLogger,
                joinPaths(key, "enchantments"), String.format("enchantments of %s", title));

        return new HItemDrop(title, probability, material, amount, enchantments, durability);
    }

    public String toString() {
        return String.format("{probability: %s, material: %s, amount: %s, enchantments: %s, durability: %s}",
                probability, material, amount, enchantments, durability);
    }

}
