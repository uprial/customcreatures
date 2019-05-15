package com.gmail.uprial.customcreatures.schema;

import com.google.common.collect.Sets;
import org.bukkit.entity.EntityType;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.entity.EntityType.*;

/*
    https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/LivingEntity.html

    AbstractHorse, AbstractVillager, Ageable, Ambient, Animals, ArmorStand, Bat, Blaze,
    Cat, CaveSpider, ChestedHorse, Chicken, Cod, ComplexLivingEntity, Cow, Creature,
    Creeper, Dolphin, Donkey, Drowned, ElderGuardian, EnderDragon, Enderman, Endermite,
    Evoker, Fish, Flying, Fox, Ghast, Giant, Golem, Guardian,
    Horse, HumanEntity, Husk, Illager, Illusioner, IronGolem, Llama, MagmaCube,
    Mob, Monster, Mule, MushroomCow, NPC, Ocelot, Panda, Parrot,
    Phantom, Pig, PigZombie, Pillager, Player, PolarBear, PufferFish, Rabbit,
    Raider, Ravager, Salmon, Sheep, Shulker, Silverfish, Skeleton, SkeletonHorse,
    Slime, Snowman, Spellcaster, Spider, Squid, Stray, TraderLlama, TropicalFish,
    Turtle, Vex, Villager, Vindicator, WanderingTrader, WaterMob, Witch, Wither,
    WitherSkeleton, Wolf, Zombie, ZombieHorse, ZombieVillager
*/
public enum HItemTypeSet {
    ANIMALS(null,
            Sets.newHashSet(CHICKEN, CAT, COD, COW, DOLPHIN, DONKEY, FOX, HORSE,
                    LLAMA, MULE, MUSHROOM_COW, OCELOT, PANDA, PARROT, PIG, POLAR_BEAR,
                    PUFFERFISH, RABBIT, SALMON, SHEEP, TROPICAL_FISH, TURTLE, WOLF, ZOMBIE_HORSE)),
    GOLEMS(null,
            Sets.newHashSet(IRON_GOLEM, SHULKER, SNOWMAN)),
    MONSTERS(null,
            Sets.newHashSet(BLAZE, CAVE_SPIDER, CREEPER, DROWNED, ENDERMAN, ENDERMITE, EVOKER, ELDER_GUARDIAN,
                    EVOKER_FANGS, GIANT, GUARDIAN, HUSK, ILLUSIONER, PHANTOM, PIG_ZOMBIE, PILLAGER,
                    RAVAGER, SILVERFISH, SKELETON, SKELETON_HORSE, SPIDER, STRAY, VEX, VINDICATOR,
                    WITCH, WITHER, WITHER_SKELETON, ZOMBIE, ZOMBIE_VILLAGER)),
    CREATURES(Sets.newHashSet(ANIMALS, GOLEMS, MONSTERS),
            Sets.newHashSet(BAT, VILLAGER, TRADER_LLAMA, WANDERING_TRADER)),
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
