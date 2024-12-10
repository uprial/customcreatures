package com.gmail.uprial.customcreatures.listeners;

import com.gmail.uprial.customcreatures.CreaturesConfig;
import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.inventory.ItemStack;

import static com.gmail.uprial.customcreatures.common.PersistenceHelper.addPersistentMetadataFlag;
import static com.gmail.uprial.customcreatures.common.PersistenceHelper.containsPersistentMetadataFlag;

public class CustomCreaturesSpawnEventListener extends AbstractCustomCreaturesEventListener {
    private static final String MK_HANDLED = "handled";

    public CustomCreaturesSpawnEventListener(CustomCreatures plugin, CustomLogger customLogger) {
        super(plugin, customLogger);
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!event.isCancelled()) {
            handleSpawnOncePerEntity(event.getEntity(), event.getSpawnReason());
        }
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // Skip respawns after The End
        for(ItemStack itemStack : event.getPlayer().getInventory().getContents()) {
            if(itemStack != null) {
                return;
            }
        }
        /*
            According to https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/entity/CreatureSpawnEvent.SpawnReason.html,
            DEFAULT - when an entity is missing a SpawnReason.
         */
        handleSpawn(event.getPlayer(), SpawnReason.DEFAULT);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntitiesLoadEvent(EntitiesLoadEvent event) {
        for(Entity entity : event.getEntities()) {
            if(entity instanceof LivingEntity) {
                /*
                    According to https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/entity/CreatureSpawnEvent.SpawnReason.html,
                    NATURAL - when something spawns from natural means.
                 */
                handleSpawnOncePerEntity((LivingEntity)entity, SpawnReason.NATURAL);
            }
        };
    }

    private void handleSpawnOncePerEntity(LivingEntity entity, SpawnReason spawnReason) {
        if(!containsPersistentMetadataFlag(plugin, entity, MK_HANDLED)) {
            handleSpawn(entity, spawnReason);

            addPersistentMetadataFlag(plugin, entity, MK_HANDLED);
        }
    }

    private void handleSpawn(LivingEntity entity, SpawnReason spawnReason) {
        CreaturesConfig creaturesConfig = plugin.getCreaturesConfig();
        // Don't try to handle an entity if there was error in loading of config.
        if (creaturesConfig != null) {
            creaturesConfig.handleSpawn(plugin, customLogger, entity, spawnReason);
        }
    }
}
