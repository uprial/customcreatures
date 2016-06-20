package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.Vector;

public class HItem {
    private final HItemFilter filter;
    private final Value speedMultiplier;

    private HItem(HItemFilter filter, Value speedMultiplier) {
        this.filter = filter;
        this.speedMultiplier = speedMultiplier;
    }

    public void handle(CustomLogger customLogger, LivingEntity entity, CreatureSpawnEvent.SpawnReason spawnReason) {
        if (null == filter || filter.pass(entity.getType(), spawnReason)) {
            applySpeedMultiplier(customLogger, entity);
        }
    }

    private void applySpeedMultiplier(CustomLogger customLogger, LivingEntity entity) {
        if (null != speedMultiplier) {
            Vector velocity = entity.getVelocity();
            double value = speedMultiplier.getValue();
            velocity.setX(velocity.getX() * value);
            velocity.setY(velocity.getY() * value);
            velocity.setZ(velocity.getZ() * value);
            entity.setVelocity(velocity);

            if(customLogger.isDebugMode()) {
                Location location = entity.getLocation();
                customLogger.debug(String.format("Multiplied speed of %s at {world: %s, x: %.0f, y: %.0f, z: %.0f} to %.2f",
                        entity.getType().toString(), location.getWorld().getName(),
                        location.getX(), location.getY(), location.getZ(), value));
            }
        }
    }

    public static HItem getFromConfig(FileConfiguration config, CustomLogger customLogger, String key) throws InvalidConfigException {
        HItemFilter filter = HItemFilter.getFromConfig(config, customLogger, key + ".filter", "filter of handler", key);
        Value speedMultiplier = Value.getFromConfig(config, customLogger, key + ".speed-multiplier", "speed multiplier of handler", key);
        if (null == speedMultiplier) {
            throw new InvalidConfigException(String.format("No modifications found for handler '%s'", key));
        }

        return new HItem(filter, speedMultiplier);
    }
}
