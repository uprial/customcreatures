package com.gmail.uprial.customcreatures;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomCreaturesPlayerTracker implements Runnable {
    private static final int INTERVAL = 10;

    private final CustomCreatures plugin;

    private static final Map<UUID, Map<Boolean, Location>> PLAYERS = new HashMap<>();
    private static boolean SIDE = true;

    public static int getInterval() {
        return INTERVAL;
    }

    public CustomCreaturesPlayerTracker(CustomCreatures plugin) {
        this.plugin = plugin;
    }

    public static Vector getPlayerMovementVector(Player player) {
        UUID uuid = player.getUniqueId();
        if(!PLAYERS.containsKey(uuid)) {
            return new Vector(0.0, 0.0, 0.0);
        } else {
            Map<Boolean, Location> bucket = PLAYERS.get(uuid);
            if(!bucket.containsKey(true) || !bucket.containsKey(false)) {
                return new Vector(0.0, 0.0, 0.0);
            } else {
                Location CurrentLocation = bucket.get(SIDE);
                Location OldLocation = bucket.get(!SIDE);

                return new Vector(
                        (CurrentLocation.getX() - OldLocation.getX()) / INTERVAL,
                        (CurrentLocation.getY() - OldLocation.getY()) / INTERVAL,
                        (CurrentLocation.getZ() - OldLocation.getZ()) / INTERVAL
                );
            }
        }
    }

    @Override
    public void run() {
        SIDE = ! SIDE;
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            Map<Boolean, Location> bucket;
            if(PLAYERS.containsKey(uuid)) {
                bucket = PLAYERS.get(uuid);
            } else {
                bucket = new HashMap<>();
                PLAYERS.put(uuid, bucket);
            }
            bucket.put(SIDE, player.getLocation());

        }
    }
}