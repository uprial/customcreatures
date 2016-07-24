package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.schema.Probability.MAX_PERCENT;

public final class HItemDurability {
    private final String title;
    private final IValue<Integer> durability;

    private HItemDurability(String title, IValue<Integer> durability) {
        this.title = title;
        this.durability = durability;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity, ItemStack itemStack) {
        Material material = itemStack.getType();

        int itemDurability = durability.getValue();
        if (customLogger.isDebugMode()) {
            customLogger.debug(String.format("Handle %s: set durability of %s of %s to %d",
                    title, material, format(entity), itemDurability));
        }
        itemStack.setDurability((short)Math.round((1.0 * material.getMaxDurability() * (MAX_PERCENT - itemDurability)) / MAX_PERCENT));
    }

    public static HItemDurability getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        IValue<Integer> durability = HValue.getIntFromConfig(config, customLogger, key, title, 0, MAX_PERCENT);
        if (durability == null) {
            return null;
        }

        return new HItemDurability(title, durability);
    }

    public String toString() {
        return durability.toString();
    }
}
