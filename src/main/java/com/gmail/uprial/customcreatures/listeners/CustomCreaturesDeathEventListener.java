package com.gmail.uprial.customcreatures.listeners;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.schema.EntityEquipmentHelper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;

import java.util.Map;

import static org.bukkit.enchantments.Enchantment.LOOT_BONUS_MOBS;

public class CustomCreaturesDeathEventListener extends AbstractCustomCreaturesEventListener {
    public CustomCreaturesDeathEventListener(CustomCreatures plugin, CustomLogger customLogger) {
        super(plugin, customLogger);
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeath(EntityDeathEvent event) {
        EntityEquipment equipment = event.getEntity().getEquipment();
        if(equipment != null) {
            Player killer = event.getEntity().getKiller();
            if (killer != null) {
                EntityEquipment killerEquipment = killer.getEquipment();
                if (killerEquipment != null) {
                    Map<Enchantment, Integer> enchantments = killerEquipment.getItemInMainHand().getEnchantments();
                    if (enchantments.containsKey(LOOT_BONUS_MOBS)) {
                        EntityEquipmentHelper.handleLootBonusMobs(plugin, equipment, enchantments.get(LOOT_BONUS_MOBS));
                    }
                }
            }
        }
    }
}
