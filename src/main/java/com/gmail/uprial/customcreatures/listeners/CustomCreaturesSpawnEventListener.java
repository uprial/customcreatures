package com.gmail.uprial.customcreatures.listeners;

import com.gmail.uprial.customcreatures.CreaturesConfig;
import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class CustomCreaturesSpawnEventListener extends AbstractCustomCreaturesEventListener {

    public CustomCreaturesSpawnEventListener(CustomCreatures plugin, CustomLogger customLogger) {
        super(plugin, customLogger);
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!event.isCancelled()) {
            handleSpawn(event.getEntity(), event.getSpawnReason());
        }
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // Skip respawns after The End
        for(ItemStack itemStack : event.getPlayer().getInventory().getContents()) {
            if(itemStack != null) {
                return;
            }
        }
        handleSpawn(event.getPlayer(), SpawnReason.DEFAULT);
    }

    private void handleSpawn(LivingEntity entity, SpawnReason spawnReason) {
        CreaturesConfig creaturesConfig = plugin.getCreaturesConfig();
        // Don't try to handle an entity if there was error in loading of config.
        if (creaturesConfig != null) {
            creaturesConfig.handleSpawn(plugin, customLogger, entity, spawnReason);
        }
    }
}
