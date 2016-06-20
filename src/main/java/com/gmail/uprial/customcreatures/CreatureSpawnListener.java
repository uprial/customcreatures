package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

class CreatureSpawnListener implements Listener {
	private final CustomCreatures plugin;
	private final CustomLogger customLogger;
	
	public CreatureSpawnListener(CustomCreatures plugin, CustomLogger customLogger) {
		this.plugin = plugin;
		this.customLogger = customLogger;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (! event.isCancelled()) {
            plugin.getCreaturesConfig().handle(customLogger, event.getEntity(), event.getSpawnReason());
        }
	}
}
