package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (! event.isCancelled()) {
            plugin.getCreaturesConfig().handle(customLogger, event.getEntity(), event.getSpawnReason());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        plugin.getCreaturesConfig().handle(customLogger, event.getPlayer(), SpawnReason.DEFAULT);
    }
}
