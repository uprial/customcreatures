package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.enchantment.EnchantmentLoader;
import com.gmail.uprial.customcreatures.schema.enchantment.IEnchantmentEnum;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReader.getEnum;

public class HItemEnchantment<T extends Enum & IEnchantmentEnum> {
    private final String title;
    private final T enchantment;
    private final IValue<Integer> level;

    private HItemEnchantment(String title, T enchantment, IValue<Integer> level) {
        this.title = title;
        this.enchantment = enchantment;
        this.level = level;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity, ItemStack itemStack) {
        for (Enchantment existsEnchantment : itemStack.getEnchantments().keySet()) {
            if (enchantment.getType().conflictsWith(existsEnchantment)) {
                customLogger.error(String.format("Can't handle %s of %s because %s conflicts with %s",
                        title, format(entity), enchantment.getType().getName(),
                        existsEnchantment.getName()));
                return ;
            }
        }
        if (! enchantment.getType().canEnchantItem(itemStack)) {
            customLogger.error(String.format("Can't handle %s of %s", title, format(entity)));
            return ;
        }

        int enchantmentLevel = this.level.getValue();

        if(customLogger.isDebugMode()) {
            customLogger.debug(String.format("Handle %s of %s: add %s with level %d",
                    title, format(entity), enchantment.getType().getName(), enchantmentLevel));
        }
        itemStack.addEnchantment(enchantment.getType(), enchantmentLevel);
    }

    public static HItemEnchantment getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (null == config.get(key)) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        Enum enchantment = getEnum(EnchantmentLoader.get(), config, joinPaths(key, "type"), String.format("enchantment type of %s", title));
        IValue<Integer> level = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "level"), String.format("level of %s", title));
        if (null == level) {
            throw new InvalidConfigException(String.format("Empty level of %s", title));
        }

        //noinspection unchecked
        return new HItemEnchantment(title, enchantment, level);
    }

    public String toString() {
        return String.format("[type: %s, level: %s]", enchantment, level);
    }
}
