package com.gmail.uprial.customcreatures.schema;

import com.google.common.collect.Sets;
import org.bukkit.entity.EntityType;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.entity.EntityType.*;

/*
    https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/LivingEntity.html (version 1.19.4)

    AbstractHorse, AbstractSkeleton, AbstractVillager, Ageable, Allay, Ambient, Animals, ArmorStand,
    Axolotl, Bat, Bee, Blaze, Breedable, Camel, Cat, CaveSpider,
    ChestedHorse, Chicken, Cod, ComplexLivingEntity, Cow, Creature, Creeper, Dolphin,
    Donkey, Drowned, ElderGuardian, EnderDragon, Enderman, Endermite, Enemy, Evoker,
    Fish, Flying, Fox, Frog, Ghast, Giant, GlowSquid, Goat,
    Golem, Guardian, Hoglin, Horse, HumanEntity, Husk, Illager, Illusioner,
    IronGolem, Llama, MagmaCube, Mob, Monster, Mule, MushroomCow, NPC,
    Ocelot, Panda, Parrot, Phantom, Pig, Piglin, PiglinAbstract, PiglinBrute,
    PigZombie, Pillager, Player, PolarBear, PufferFish, Rabbit, Raider, Ravager,
    Salmon, Sheep, Shulker, Silverfish, Skeleton, SkeletonHorse, Slime, Sniffer,
    Snowman, Spellcaster, Spider, Squid, Steerable, Stray, Strider, Tadpole,
    Tameable, TraderLlama, TropicalFish, Turtle, Vex, Villager, Vindicator, WanderingTrader,
    Warden, WaterMob, Witch, Wither, WitherSkeleton, Wolf, Zoglin, Zombie,
    ZombieHorse, ZombieVillager
*/
public enum HItemTypeSet {
    ANIMALS(null,
            Sets.newHashSet(ALLAY, AXOLOTL, BAT, BEE, CAMEL, CAT, CHICKEN, COD,
                    COW, DOLPHIN, DONKEY, FOX, FROG, GLOW_SQUID, GOAT, HORSE,
                    LLAMA, MULE, MUSHROOM_COW, OCELOT, PANDA, PARROT, PIG, POLAR_BEAR,
                    PUFFERFISH, RABBIT, SALMON, SHEEP, SNIFFER, SQUID, STRIDER, TADPOLE,
                    TROPICAL_FISH, TURTLE, WOLF, ZOMBIE_HORSE)),
    GOLEMS(null,
            Sets.newHashSet(IRON_GOLEM, SNOWMAN)),
    MONSTERS(null,
            Sets.newHashSet(BLAZE, CAVE_SPIDER, CREEPER, DROWNED, ELDER_GUARDIAN, ENDER_DRAGON, ENDERMAN, ENDERMITE,
                    EVOKER, EVOKER_FANGS, GHAST, GIANT, GUARDIAN, HOGLIN, HUSK, ILLUSIONER,
                    MAGMA_CUBE, PHANTOM, PIGLIN, PIGLIN_BRUTE, PILLAGER, RAVAGER, SHULKER, SILVERFISH,
                    SKELETON, SKELETON_HORSE, SLIME, SPIDER, STRAY, VEX, VINDICATOR, WARDEN,
                    WITCH, WITHER, WITHER_SKELETON, ZOGLIN, ZOMBIE, ZOMBIE_VILLAGER, ZOMBIFIED_PIGLIN)),

    // An unused set that underlines we didn't forget of some creatures.
    CREATURES(Sets.newHashSet(ANIMALS, GOLEMS, MONSTERS),
            Sets.newHashSet(PLAYER, TRADER_LLAMA, VILLAGER, WANDERING_TRADER)),

    // https://minecraft.gamepedia.com/Zombie_(disambiguation)
    ZOMBIES(null,
            Sets.newHashSet(DROWNED, GIANT, HUSK, PIGLIN, PIGLIN_BRUTE, WITHER_SKELETON, ZOMBIE, ZOMBIE_VILLAGER,
                    ZOMBIFIED_PIGLIN)),
    // https://minecraft.gamepedia.com/Skeleton_(disambiguation)
    SKELETONS(null,
            Sets.newHashSet(SKELETON, STRAY, WITHER_SKELETON)),
    FLYING_MOBS(null,
            Sets.newHashSet(ALLAY, BAT, BEE, ENDER_DRAGON, GHAST, PARROT, PHANTOM, VEX, WITHER));

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
