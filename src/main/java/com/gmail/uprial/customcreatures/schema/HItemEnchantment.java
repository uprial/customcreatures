package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.enchantment.EnchantmentLoader;
import com.gmail.uprial.customcreatures.schema.enchantment.IEnchantmentEnum;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getEnum;

public final class HItemEnchantment<T extends Enum & IEnchantmentEnum> {
    private final String title;
    private final T enchantment;
    private final IValue<Integer> level;

    private HItemEnchantment(String title, T enchantment, IValue<Integer> level) {
        this.title = title;
        this.enchantment = enchantment;
        this.level = level;
    }

    public void apply(CustomLogger customLogger, Entity entity, ItemStack itemStack) {
        if(customLogger.isDebugMode()) {
            for (Enchantment existsEnchantment : itemStack.getEnchantments().keySet()) {
                if (enchantment.getType().conflictsWith(existsEnchantment)) {
                    customLogger.debug(String.format("Handle %s of %s: %s conflicts with %s",
                            title, format(entity), enchantment.getType().toString(),
                            existsEnchantment));
                }
            }
        }
        if (! enchantment.getType().canEnchantItem(itemStack)) {
            customLogger.error(String.format("Can't handle %s of %s", title, format(entity)));
            return ;
        }

        int enchantmentLevel = level.getValue();

        if(customLogger.isDebugMode()) {
            customLogger.debug(String.format("Handle %s of %s: add %s with level %d",
                    title, format(entity), enchantment.getType().toString(), enchantmentLevel));
        }

        if (enchantmentLevel > 0) {
            try {
                itemStack.addUnsafeEnchantment(enchantment.getType(), enchantmentLevel);
            } catch (IllegalArgumentException e) {
                customLogger.error(String.format("Can't handle %s of %s: %s",
                        title, format(entity), e.getMessage()));
            }
        }
    }

    public static HItemEnchantment getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        Enum enchantment = getEnum(EnchantmentLoader.get(), config, joinPaths(key, "type"), String.format("enchantment type of %s", title));
        IValue<Integer> level = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "level"),
                String.format("level of %s", title), 0, 100);
        if (level == null) {
            throw new InvalidConfigException(String.format("Empty level of %s", title));
        }

        //noinspection unchecked
        return new HItemEnchantment(title, enchantment, level);
    }

    public String toString() {
        return String.format("{type: %s, level: %s}", enchantment, level);
    }
}
