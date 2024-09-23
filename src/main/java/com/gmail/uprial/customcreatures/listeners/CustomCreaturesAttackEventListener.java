package com.gmail.uprial.customcreatures.listeners;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.schema.HItemAttributes.getAttackDamageMultiplier;

public class CustomCreaturesAttackEventListener extends AbstractCustomCreaturesEventListener {
    public CustomCreaturesAttackEventListener(CustomCreatures plugin, CustomLogger customLogger) {
        super(plugin, customLogger);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (! event.isCancelled()) {
            Entity source = event.getDamager();
            LivingEntity livingSource = null;

            if (source instanceof Projectile) {
                final Projectile projectile = (Projectile) source;
                final ProjectileSource projectileShooter = projectile.getShooter();
                if (projectileShooter instanceof LivingEntity) {
                    livingSource = (LivingEntity) projectileShooter;
                }
            } else if (source instanceof LivingEntity) {
                livingSource = (LivingEntity) source;
            }

            if (livingSource != null) {
                //noinspection LocalVariableNamingConvention
                final Double attackDamageMultiplier = getAttackDamageMultiplier(plugin, livingSource);
                if (attackDamageMultiplier != null) {
                    final double damage = event.getDamage();
                    final double newDamage = damage * attackDamageMultiplier;
                    if(customLogger.isDebugMode()) {
                        customLogger.debug(String.format("Handle attack damage multiplier: change damage of %s on %s from %.2f to %.2f",
                                format(livingSource), format(event.getEntity()), damage, newDamage));
                    }
                    event.setDamage(newDamage);
                }
            }
        }
    }
}
