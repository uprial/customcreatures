package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerRespawnEvent;

class CustomCreaturesEventListener implements Listener {
    private static final int HANDLER_DELAY = 1;

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
            if (event.getEntity() instanceof Skeleton) {
                defer(new CreatureSpawnTask(this, event));
            } else {
                handleCreatureSpawn(event);
            }
        }
    }

    public void handleCreatureSpawn(CreatureSpawnEvent event) {
        plugin.getCreaturesConfig().handle(plugin, customLogger, event.getEntity(), event.getSpawnReason());
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        defer(new PlayerRespawnTask(this, event));
    }

    public void handlePlayerRespawn(PlayerRespawnEvent event) {
        plugin.getCreaturesConfig().handle(plugin, customLogger, event.getPlayer(), SpawnReason.DEFAULT);
    }

    private void defer(Runnable task) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, HANDLER_DELAY);
    }

}
