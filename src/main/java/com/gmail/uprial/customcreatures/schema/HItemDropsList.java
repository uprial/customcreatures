package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;
import static com.gmail.uprial.customcreatures.config.ConfigReaderLists.getItemsList;

public final class HItemDropsList {

    private final List<HItemDrop> itemDrops;

    private HItemDropsList(List<HItemDrop> itemDrops) {
        this.itemDrops = itemDrops;
    }

    public void apply(CustomLogger customLogger, EntityDeathEvent event) {
        for (HItemDrop itemDrop : itemDrops) {
            itemDrop.apply(customLogger, event);
        }
    }

    public static HItemDropsList getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        Set<String> subKeys = getItemsList(config, customLogger, key, title);
        if (subKeys == null) {
            return null;
        }

        List<HItemDrop> itemDrops = new ArrayList<>();

        for(String subKey : subKeys) {
            itemDrops.add(HItemDrop.getFromConfig(config, customLogger, subKey, String.format("drop '%s' in %s", subKey, title)));
        }

        return new HItemDropsList(itemDrops);
    }

    public String toString() {
        return String.format("[%s]", joinStrings(", ", itemDrops));
    }
}
