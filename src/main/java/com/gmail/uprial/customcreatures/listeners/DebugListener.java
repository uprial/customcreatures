package com.gmail.uprial.customcreatures.listeners;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;

import java.util.Map;

import static com.gmail.uprial.customcreatures.common.Formatter.format;

public class DebugListener implements Listener {
    private final CustomLogger customLogger;

    public DebugListener(final CustomLogger customLogger) {
        this.customLogger = customLogger;
    }

    @SuppressWarnings("unused")
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
}
