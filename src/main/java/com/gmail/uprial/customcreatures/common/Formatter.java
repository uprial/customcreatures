package com.gmail.uprial.customcreatures.common;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public final class Formatter {
    public static String format(Entity entity) {
        Location location = entity.getLocation();
        return String.format("%s[world: %s, x: %.0f, y: %.0f, z: %.0f]",
                entity.getType().toString(),
                (location.getWorld() != null) ? location.getWorld().getName() : "empty",
                location.getX(), location.getY(), location.getZ());
    }

    public static String format(PotionEffect effect) {
        return String.format("%s[strength:%d, duration:%d]",
                effect.getType().getName(), effect.getAmplifier(), effect.getDuration());
    }

    public static String format(Vector vector) {
        return String.format("[x: %.2f, y: %.2f, z: %.2f, len: %.2f]",
                vector.getX(), vector.getY(), vector.getZ(), vector.length());
    }
}
