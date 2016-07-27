package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerRespawnEvent;

class CustomCreaturesEventListener implements Listener {
    private final CustomCreatures plugin;
    private final CustomLogger customLogger;

    CustomCreaturesEventListener(CustomCreatures plugin, CustomLogger customLogger) {
        this.plugin = plugin;
        this.customLogger = customLogger;
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (! event.isCancelled()) {
            handle(event.getEntity(), event.getSpawnReason());
        }
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        handle(event.getPlayer(), SpawnReason.DEFAULT);
    }

    private void handle(LivingEntity entity, SpawnReason spawnReason) {
        CreaturesConfig creaturesConfig = plugin.getCreaturesConfig();
        // Don't try to handle an entity if there was error in loading of config.
        if (creaturesConfig != null) {
            creaturesConfig.handle(plugin, customLogger, entity, spawnReason);
        }
    }
}
