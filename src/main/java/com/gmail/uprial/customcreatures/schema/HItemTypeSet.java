package com.gmail.uprial.customcreatures.schema;

import com.google.common.collect.Sets;
import org.bukkit.entity.EntityType;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.entity.EntityType.*;

/*
    https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/LivingEntity.html

    Ageable, Ambient, Animals, ArmorStand, Bat, Blaze, CaveSpider, Chicken,
    ComplexLivingEntity, Cow, Creature, Creeper, EnderDragon, Enderman, Endermite,
    Flying, Ghast, Giant, Golem, Guardian, Horse, HumanEntity, IronGolem, MagmaCube,
    Monster, MushroomCow, NPC, Ocelot, Pig, PigZombie, Player, PolarBear, Rabbit,
    Sheep, Shulker, Silverfish, Skeleton, Slime, Snowman, Spider, Squid, Villager,
    WaterMob, Witch, Wither, Wolf, Zombie
*/
public enum HItemTypeSet {
    ANIMALS(null,
            Sets.newHashSet(CHICKEN, COW, HORSE,
                    MUSHROOM_COW, OCELOT, PIG, POLAR_BEAR,
                    RABBIT, SHEEP, WOLF)),
    GOLEMS(null,
            Sets.newHashSet(IRON_GOLEM, SHULKER, SNOWMAN)),
    MONSTERS(null,
            Sets.newHashSet(BLAZE, CAVE_SPIDER, CREEPER, ENDERMAN, ENDERMITE, GIANT, GUARDIAN, PIG_ZOMBIE,
                    SILVERFISH, SKELETON, SPIDER, WITCH, WITHER, ZOMBIE)),
    CREATURES(Sets.newHashSet(ANIMALS, GOLEMS, MONSTERS),
            Sets.newHashSet(BAT, VILLAGER)),
    FLYING(null,
            Sets.newHashSet(ENDER_DRAGON, GHAST));

    final Set<EntityType> entityTypes;
    final Set<HItemTypeSet> subSets;

    private Set<EntityType> allEntityTypesCache = null;

    HItemTypeSet(Set<HItemTypeSet> subSets, Set<EntityType> entityTypes) {
        this.entityTypes = (entityTypes == null) ? new HashSet<EntityType>() : entityTypes;
        this.subSets = (subSets == null) ? new HashSet<HItemTypeSet>() : subSets;
    }

    public boolean isContains(EntityType entityType) {
        if (allEntityTypesCache == null) {
            allEntityTypesCache = getAllEntityTypes();
        }

        return allEntityTypesCache.contains(entityType);
    }

    Set<EntityType> getAllEntityTypes() {
        Set<EntityType> allEntityTypes = new HashSet<>();
        for (EntityType entityType : entityTypes) {
            allEntityTypes.add(entityType);
        }
        for(HItemTypeSet subSet : subSets) {
            allEntityTypes.addAll(subSet.getAllEntityTypes());
        }

        return allEntityTypes;
    }
}
