package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;

import static com.gmail.uprial.customcreatures.common.PersistenceHelper.*;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;

public final class HItem {
    private final String name;
    private final HItemFilter filter;
    private final HItemEffectsList effects;
    private final HItemAttributes attributes;
    private final HItemEquipment equipment;
    private final HItemDropsList drops;
    private final HItemDropExp dropExp;
    private final HItemEntitySpecificAttributes entitySpecificAttributes;

    private static final String MK_HANDLER_PREFIX = "handler-";

    private HItem(String name,
                  HItemFilter filter,
                  HItemEffectsList effects,
                  HItemAttributes attributes,
                  HItemEquipment equipment,
                  HItemDropsList drops,
                  HItemDropExp dropExp,
                  HItemEntitySpecificAttributes entitySpecificAttributes) {
        this.name = name;
        this.filter = filter;
        this.effects = effects;
        this.attributes = attributes;
        this.equipment = equipment;
        this.drops = drops;
        this.dropExp = dropExp;
        this.entitySpecificAttributes = entitySpecificAttributes;
    }

    public void handleSpawn(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity, SpawnReason spawnReason) {
        if (filter.isPassed(entity.getType(), spawnReason, entity.getWorld().getName())) {
            addPersistentMetadataFlag(plugin, entity, getHandlerMetadataKey());

            applyAttributes(plugin, customLogger, entity);
            applyEffects(customLogger, entity);
            applyEquipment(customLogger, entity);
            applyEntitySpecificAttributes(customLogger, entity);
        }
    }

    public void handleDeath(CustomCreatures plugin, CustomLogger customLogger, EntityDeathEvent event, int lootBonusMobs) {
        if (containsPersistentMetadataFlag(plugin, event.getEntity(), getHandlerMetadataKey())) {
            applyDrops(customLogger, event, lootBonusMobs);
            applyDropExp(customLogger, event);
        }
    }

    private void applyAttributes(CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        if (attributes != null) {
            attributes.apply(plugin, customLogger, entity, name);
        }
    }

    private void applyEffects(CustomLogger customLogger, LivingEntity entity) {
        if (effects != null) {
            effects.apply(customLogger, entity);
        }
    }

    private void applyEquipment(CustomLogger customLogger, LivingEntity entity) {
        if (equipment != null) {
            equipment.apply(customLogger, entity);
        }
    }

    private void applyDrops(CustomLogger customLogger, EntityDeathEvent event, int lootBonusMobs) {
        if (drops != null) {
            drops.apply(customLogger, event, lootBonusMobs);
        }
    }

    private void applyDropExp(CustomLogger customLogger, EntityDeathEvent event) {
        if (dropExp != null) {
            dropExp.apply(customLogger, event);
        }
    }

    private void applyEntitySpecificAttributes(CustomLogger customLogger, Entity entity) {
        if (entitySpecificAttributes != null) {
            entitySpecificAttributes.apply(customLogger, entity);
        }
    }

    public static HItem getFromConfig(FileConfiguration config, CustomLogger customLogger, String key) throws InvalidConfigException {
        HItemFilter filter = HItemFilter.getFromConfig(config, customLogger, joinPaths(key, "filter"),
                String.format("filter of handler '%s'", key));
        HItemEffectsList effectsList = HItemEffectsList.getFromConfig(config, customLogger, joinPaths(key, "effects"),
                String.format("effects of handler '%s'", key));
        HItemAttributes attributes = HItemAttributes.getFromConfig(config, customLogger, joinPaths(key, "attributes"),
                String.format("attributes of handler '%s'", key));
        HItemEquipment equipment = HItemEquipment.getFromConfig(config, customLogger, joinPaths(key, "equipment"),
                String.format("equipment of handler '%s'", key));
        HItemDropsList drops = HItemDropsList.getFromConfig(config, customLogger, joinPaths(key, "drops"),
                String.format("drops of handler '%s'", key));
        HItemDropExp dropExp = HItemDropExp.getFromConfig(config, customLogger, joinPaths(key, "drop-exp"),
                String.format("drop exp of handler '%s'", key));
        HItemEntitySpecificAttributes entitySpecificAttributes = HItemEntitySpecificAttributes.getFromConfig(config, customLogger,
                filter,
                joinPaths(key, "entity-specific-attributes"),
                String.format("entity-specific attributes of handler '%s'", key));

        if ((attributes == null)
                && (effectsList == null)
                && (equipment == null)
                && (drops == null)
                && (dropExp == null)
                && (entitySpecificAttributes == null)) {
            throw new InvalidConfigException(String.format("No modifications found for handler '%s'", key));
        }

        return new HItem(key, filter, effectsList, attributes, equipment, drops, dropExp, entitySpecificAttributes);
    }

    public String toString() {
        return String.format("{name: %s, filter: %s, effects: %s, " +
                        "attributes: %s, equipment: %s, drops: %s, " +
                        "drop-exp: %s, entity-specific-attributes: %s}",
                name, filter, effects,
                attributes, equipment, drops,
                dropExp, entitySpecificAttributes);
    }

    private String getHandlerMetadataKey() {
        return MK_HANDLER_PREFIX + name;
    }
}
