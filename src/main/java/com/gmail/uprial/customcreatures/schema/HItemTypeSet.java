package com.gmail.uprial.customcreatures.schema;

import com.google.common.collect.Sets;
import org.bukkit.entity.EntityType;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.entity.EntityType.*;

/*
    https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/LivingEntity.html (version 1.16.5)

    AbstractHorse, AbstractVillager, Ageable, Ambient, Animals, ArmorStand, Bat, Bee,
    Blaze, Breedable, Cat, CaveSpider, ChestedHorse, Chicken, Cod, ComplexLivingEntity,
    Cow, Creature, Creeper, Dolphin, Donkey, Drowned, ElderGuardian, EnderDragon,
    Enderman, Endermite, Evoker, Fish, Flying, Fox, Ghast, Giant,
    Golem, Guardian, Hoglin, Horse, HumanEntity, Husk, Illager, Illusioner,
    IronGolem, Llama, MagmaCube, Mob, Monster, Mule, MushroomCow, NPC,
    Ocelot, Panda, Parrot, Phantom, Pig, Piglin, PiglinAbstract, PiglinBrute,
    PigZombie, Pillager, Player, PolarBear, PufferFish, Rabbit, Raider, Ravager,
    Salmon, Sheep, Shulker, Silverfish, Skeleton, SkeletonHorse, Slime, Snowman,
    Spellcaster, Spider, Squid, Steerable, Stray, Strider, Tameable, TraderLlama,
    TropicalFish, Turtle, Vex, Villager, Vindicator, WanderingTrader, WaterMob, Witch,
     Wither, WitherSkeleton, Wolf, Zoglin, Zombie, ZombieHorse, ZombieVillager
*/
public enum HItemTypeSet {
    ANIMALS(null,
            Sets.newHashSet(BAT, BEE, CHICKEN, CAT, COD, COW, DOLPHIN, DONKEY,
                    FOX, HOGLIN, HORSE, LLAMA, MULE, MUSHROOM_COW, OCELOT, PANDA,
                    PARROT, PIG, POLAR_BEAR, PUFFERFISH, RABBIT, SALMON, SHEEP, SQUID,
                    STRIDER, TROPICAL_FISH, TURTLE, WOLF, ZOMBIE_HORSE)),
    GOLEMS(null,
            Sets.newHashSet(IRON_GOLEM, SNOWMAN)),
    MONSTERS(null,
            Sets.newHashSet(BLAZE, CAVE_SPIDER, CREEPER, DROWNED, ELDER_GUARDIAN, ENDER_DRAGON, ENDERMAN, ENDERMITE,
                    EVOKER,  EVOKER_FANGS, GIANT, GUARDIAN, HUSK, ILLUSIONER, MAGMA_CUBE,
                    PHANTOM, PIGLIN, PIGLIN_BRUTE, PILLAGER, RAVAGER, SILVERFISH, SHULKER, SKELETON,
                    SKELETON_HORSE, SLIME, SPIDER, STRAY, VEX, VINDICATOR, WITCH, WITHER,
                    WITHER_SKELETON, ZOGLIN, ZOMBIE, ZOMBIE_VILLAGER, ZOMBIFIED_PIGLIN)),

    // An unused set that underlines we didn't forget of some creatures.
    CREATURES(Sets.newHashSet(ANIMALS, GOLEMS, MONSTERS),
            Sets.newHashSet(PLAYER, TRADER_LLAMA, VILLAGER, WANDERING_TRADER)),

    // https://minecraft.gamepedia.com/Zombie_(disambiguation)
    ZOMBIES(null,
            Sets.newHashSet(DROWNED, GIANT, HUSK, PIGLIN, PIGLIN_BRUTE, ZOMBIE, ZOMBIE_VILLAGER)),
    // https://minecraft.gamepedia.com/Skeleton_(disambiguation)
    SKELETONS(null,
            Sets.newHashSet(SKELETON, STRAY, WITHER_SKELETON)),
    FLYING_MOBS(null,
            Sets.newHashSet(BAT, BEE, ENDER_DRAGON, GHAST, PARROT, PHANTOM, VEX, WITHER));

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
