package com.gmail.uprial.customcreatures.listeners;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.UUID;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.MetadataHelper.getMetadata;
import static com.gmail.uprial.customcreatures.common.MetadataHelper.setMetadata;
import static com.gmail.uprial.customcreatures.common.Utils.GRAVITY_ACCELERATION;
import static com.gmail.uprial.customcreatures.common.Utils.SERVER_TICKS_IN_SECOND;
import static com.gmail.uprial.customcreatures.schema.HItemAttributes.getFinalAttackDamage;
import static com.gmail.uprial.customcreatures.schema.HItemAttributes.getOriginalFollowRange;

public class CustomCreaturesAttackEventListener extends AbstractCustomCreaturesEventListener {
    private static final String MK_TARGET_PLAYER_UUID = "target-player-uuid";

    public CustomCreaturesAttackEventListener(CustomCreatures plugin, CustomLogger customLogger) {
        super(plugin, customLogger);
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTargetEvent(EntityTargetEvent event) {
        if (!event.isCancelled()) {
            Entity source = event.getEntity();
            // Performance improvement: ProjectileSource instead of LivingEntity
            if (source instanceof ProjectileSource) {
                LivingEntity projectileSource = (LivingEntity) source;
                Double originalFollowRange = getOriginalFollowRange(projectileSource);
                if (originalFollowRange != null) {
                    Entity target = event.getTarget();
                    if (target instanceof Player) {
                        Player targetPlayer = (Player) target;
                        setMetadata(plugin, projectileSource, MK_TARGET_PLAYER_UUID, targetPlayer.getUniqueId());
                    } else {
                        // Clear the target
                        setMetadata(plugin, projectileSource, MK_TARGET_PLAYER_UUID, null);
                    }
                }
            }
        }
    }

    @SuppressWarnings({"unused", "MethodMayBeStatic"})
    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
        if (!event.isCancelled()) {
            Projectile projectile = event.getEntity();
            // Fireballs and shulker's bullets don't follow the gravity rules.
            if (!(projectile instanceof Fireball) && !(projectile instanceof ShulkerBullet)) {
                ProjectileSource shooter = projectile.getShooter();
                if (shooter instanceof LivingEntity) {
                    LivingEntity projectileSource = (LivingEntity) shooter;
                    Double originalFollowRange = getOriginalFollowRange(projectileSource);
                    if (originalFollowRange != null) {
                        UUID targetPlayerUUID = getMetadata(projectileSource, MK_TARGET_PLAYER_UUID);
                        if (targetPlayerUUID != null) {
                            Player targetPlayer = plugin.getOnlinePlayerByUUID(targetPlayerUUID);
                            if (targetPlayer != null) {
                                fixProjectileTrajectory(projectileSource, projectile, originalFollowRange, targetPlayer);
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings({"unused", "MethodMayBeStatic"})
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (! event.isCancelled()) {
            Entity source = event.getDamager();
            LivingEntity livingSource = null;

            if (source instanceof Projectile) {
                Projectile projectile = (Projectile) source;
                ProjectileSource projectileShooter = projectile.getShooter();
                if (projectileShooter instanceof LivingEntity) {
                    livingSource = (LivingEntity) projectileShooter;
                }
            } else if (source instanceof LivingEntity) {
                livingSource = (LivingEntity) source;
            }

            if (livingSource != null) {
                Double finalAttackDamage = getFinalAttackDamage(livingSource);
                if (finalAttackDamage != null) {
                    if(customLogger.isDebugMode()) {
                        customLogger.debug(String.format("Modify damage of %s on %s from %.2f to %.2f",
                                format(livingSource), format(event.getEntity()), event.getDamage(), finalAttackDamage));
                    }
                    event.setDamage(finalAttackDamage);
                }
            }
        }
    }

    private void fixProjectileTrajectory(LivingEntity projectileSource, Projectile projectile, Double originalFollowRange, Player targetPlayer) {
        Location targetLocation = targetPlayer.getEyeLocation();
        if (targetLocation.distance(projectile.getLocation()) > originalFollowRange) {
            Vector projectileVelocity = projectile.getVelocity();
            Location projectileLocation = projectile.getLocation();

            double ticksInFly = originalFollowRange / projectileVelocity.length();

            double vx = (targetLocation.getX() - projectileLocation.getX()) / ticksInFly;
            double vz = (targetLocation.getZ() - projectileLocation.getZ()) / ticksInFly;

            /*
            y2 = y1 + vy * t + g * t^2 / 2 =>
                vy = (y2 - g * t^2 / 2 - y1) / t

            g = GRAVITY_ACCELERATION
            t = ticksInFly (ticks) / t2s (ticks/s)

            Example:
              y1 = 0
              y2 = 0
              ticksInFly = 10.0

              t = 10.0 / 20.0 = 0.5,
              vy = (0 - (- 20.0 * 0.5^2 / 2) - 0) / 0.5 =:= 20.0 / 4 =:= 5 (m/s)
             */

            double y1 = 0.0;
            double y2 = targetLocation.getY() - projectileLocation.getY();
            double t = ticksInFly / SERVER_TICKS_IN_SECOND;
            double vy = ((y2 - ((GRAVITY_ACCELERATION * (t * t)) / 2.0) - y1) / t) / SERVER_TICKS_IN_SECOND;

            Vector newVelocity = new Vector(vx, vy, vz);
            projectile.setVelocity(newVelocity);

            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Modify projective's velocity of %s on %s from %s to %s",
                        format(projectileSource), format(targetPlayer), format(projectileVelocity), format(newVelocity)));
            }
        }
    }
}
