package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;
import static com.gmail.uprial.customcreatures.config.ConfigReaderLists.getItemsList;

public final class HItemEnchantmentsList {

    private final List<HItemEnchantment> itemEnchantments;

    private HItemEnchantmentsList(List<HItemEnchantment> itemEnchantments) {
        this.itemEnchantments = itemEnchantments;
    }

    public void apply(CustomLogger customLogger, LivingEntity entity, ItemStack itemStack) {
        for (HItemEnchantment itemEnchantment : itemEnchantments) {
            itemEnchantment.apply(customLogger, entity, itemStack);
        }
    }

    public static HItemEnchantmentsList getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        Set<String> subKeys = getItemsList(config, customLogger, key, title);
        if (subKeys == null) {
            return null;
        }

        List<HItemEnchantment> itemEnchantments = new ArrayList<>();

        for(String subKey : subKeys) {
            itemEnchantments.add(HItemEnchantment.getFromConfig(config, customLogger, subKey, String.format("enchantment '%s' in %s", subKey, title)));
        }

        return new HItemEnchantmentsList(itemEnchantments);
    }

    public String toString() {
        return String.format("{%s}", joinStrings(", ", itemEnchantments));
    }
}
