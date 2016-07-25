package com.gmail.uprial.customcreatures;

import org.bukkit.event.player.PlayerRespawnEvent;

class PlayerRespawnTask implements Runnable {
    private final CustomCreaturesEventListener eventListener;
    private final PlayerRespawnEvent event;

    PlayerRespawnTask(CustomCreaturesEventListener eventListener, PlayerRespawnEvent event) {
        this.eventListener = eventListener;
        this.event = event;
    }

    @Override
    public void run() {
        eventListener.handlePlayerRespawn(event);
    }
}
