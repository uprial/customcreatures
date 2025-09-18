package com.gmail.uprial.customcreatures.listeners;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static com.gmail.uprial.customcreatures.common.Formatter.format;

public class DebugListener implements Listener {
    private final CustomLogger customLogger;

    public DebugListener(final CustomLogger customLogger) {
        this.customLogger = customLogger;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!event.isCancelled()
                && (event.getEntity() instanceof LivingEntity)
                && (event.getDamageSource().getCausingEntity() instanceof Player)) {

            customLogger.info(String.format("%s damaged: %.2f/%.2f",
                    format(event.getEntity()),
                    event.getDamage(), event.getFinalDamage()));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if((!event.useItemInHand().equals(Event.Result.DENY))
                && (!event.useInteractedBlock().equals(Event.Result.DENY))
                && (event.getAction() == Action.RIGHT_CLICK_BLOCK)
                && (event.getMaterial() == Material.FLINT_AND_STEEL)
                && (event.getClickedBlock() != null)
                && (event.getClickedBlock().getType().equals(Material.SCAFFOLDING))) {

            for(final EntityType entityType : EntityType.values()) {
                if(entityType.isSpawnable()) {
                    customLogger.info(String.format("Summoning %s...", entityType));
                    final Entity entity = event.getClickedBlock().getWorld()
                            .spawnEntity(event.getClickedBlock().getLocation(),
                                    entityType);
                    try {
                        customLogger.info(format(entity));
                    } finally {
                        entity.remove();
                    }
                }
            }
        }
    }
}
