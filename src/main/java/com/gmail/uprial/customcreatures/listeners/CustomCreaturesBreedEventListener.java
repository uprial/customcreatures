package com.gmail.uprial.customcreatures.listeners;

import com.gmail.uprial.customcreatures.CreaturesConfig;
import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityBreedEvent;

public class CustomCreaturesBreedEventListener extends AbstractCustomCreaturesEventListener {
    public CustomCreaturesBreedEventListener(final CustomCreatures plugin,
                                             final CustomLogger customLogger) {
        super(plugin, customLogger);
    }
    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityBreed(EntityBreedEvent event) {
        if(!event.isCancelled()) {
            final CreaturesConfig creaturesConfig = plugin.getCreaturesConfig();
            // Don't try to handle an entity if there was error in loading of config.
            if (creaturesConfig != null) {
                creaturesConfig.handleBreed(customLogger, event);
            }
        }
    }
}
