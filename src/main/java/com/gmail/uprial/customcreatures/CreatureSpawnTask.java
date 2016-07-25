package com.gmail.uprial.customcreatures;

import org.bukkit.event.entity.CreatureSpawnEvent;

class CreatureSpawnTask implements Runnable {
    private final CustomCreaturesEventListener eventListener;
    private final CreatureSpawnEvent event;

    CreatureSpawnTask(CustomCreaturesEventListener eventListener, CreatureSpawnEvent event) {
        this.eventListener = eventListener;
        this.event = event;
    }

    @Override
    public void run() {
        eventListener.handleCreatureSpawn(event);
    }
}
