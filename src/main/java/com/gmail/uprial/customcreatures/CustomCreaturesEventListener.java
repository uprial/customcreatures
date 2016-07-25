package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerRespawnEvent;

class CustomCreaturesEventListener implements Listener {
    private final static int RESPAWN_HADNLER_DELAY = 1;

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
            plugin.getCreaturesConfig().handle(plugin, customLogger, event.getEntity(), event.getSpawnReason());
        }
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        PlayerRespawnTask task = new PlayerRespawnTask(this, event);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, RESPAWN_HADNLER_DELAY);
    }

    public void onPlayerRespawnDelayed(PlayerRespawnEvent event) {
        plugin.getCreaturesConfig().handle(plugin, customLogger, event.getPlayer(), SpawnReason.DEFAULT);
    }

}
