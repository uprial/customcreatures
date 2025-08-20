package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.enums.EnchantmentEnum;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getEnum;

public final class HItemEnchantment {
    private final String title;
    private final EnchantmentEnum enchantment;
    private final IValue<Integer> level;
    private final SchedulesList schedules;

    private HItemEnchantment(String title, EnchantmentEnum enchantment, IValue<Integer> level, SchedulesList schedules) {
        this.title = title;
        this.enchantment = enchantment;
        this.level = level;
        this.schedules = schedules;
    }

    public void apply(CustomLogger customLogger, Entity entity, ItemStack itemStack) {
        if(schedules.isPassed()) {

            if (customLogger.isDebugMode()) {
                final Set<Enchantment> existsEnchantments;
                if (itemStack.getItemMeta() instanceof EnchantmentStorageMeta) {
                    final EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                    existsEnchantments = itemMeta.getStoredEnchants().keySet();
                } else {
                    existsEnchantments = itemStack.getEnchantments().keySet();
                }
                for (final Enchantment existsEnchantment : existsEnchantments) {
                    if (enchantment.getType().conflictsWith(existsEnchantment)) {
                        customLogger.debug(String.format("Handle %s of %s: %s conflicts with %s",
                                title, format(entity), enchantment.getType(),
                                existsEnchantment));
                    }
                }
            }

            if (!(itemStack.getItemMeta() instanceof EnchantmentStorageMeta)) {
                if (!enchantment.getType().canEnchantItem(itemStack)) {
                    customLogger.error(String.format("Can't handle %s of %s", title, format(entity)));
                    return;
                }
            }

            final int enchantmentLevel = level.getValue();

            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s of %s: add %s with level %d",
                        title, format(entity), enchantment.getType().toString(), enchantmentLevel));
            }

            if (enchantmentLevel > 0) {
                try {
                    if (itemStack.getItemMeta() instanceof EnchantmentStorageMeta) {
                        final EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                        itemMeta.addStoredEnchant(enchantment.getType(), enchantmentLevel, true);
                        itemStack.setItemMeta(itemMeta);
                    } else {
                        itemStack.addUnsafeEnchantment(enchantment.getType(), enchantmentLevel);
                    }
                } catch (IllegalArgumentException e) {
                    customLogger.error(String.format("Can't handle %s of %s: %s",
                            title, format(entity), e.getMessage()));
                }
            }
        }
    }

    public static HItemEnchantment getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        final EnchantmentEnum enchantment = getEnum(EnchantmentEnum.class, config, joinPaths(key, "type"), String.format("enchantment type of %s", title));
        final IValue<Integer> level = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "level"),
                String.format("level of %s", title), 0, 100);
        if (level == null) {
            throw new InvalidConfigException(String.format("Empty level of %s", title));
        }

        final SchedulesList schedules = SchedulesList.getFromConfig(config, customLogger,
                joinPaths(key, "schedules"), String.format("schedules of %s", title));

        return new HItemEnchantment(title, enchantment, level, schedules);
    }

    public String toString() {
        return String.format("{type: %s, level: %s, schedules: %s}", enchantment, level, schedules);
    }
}
