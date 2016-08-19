package com.gmail.uprial.customcreatures.listeners;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.event.Listener;

@SuppressWarnings("AbstractClassWithoutAbstractMethods")
abstract class AbstractCustomCreaturesEventListener implements Listener {
    final CustomCreatures plugin;
    final CustomLogger customLogger;

    AbstractCustomCreaturesEventListener(CustomCreatures plugin, CustomLogger customLogger) {
        this.plugin = plugin;
        this.customLogger = customLogger;
    }
}
