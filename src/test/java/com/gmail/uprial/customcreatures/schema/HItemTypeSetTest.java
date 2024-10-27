package com.gmail.uprial.customcreatures.schema;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.entity.EntityType;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.gmail.uprial.customcreatures.schema.HItemTypeSet.*;
import static org.bukkit.entity.EntityType.*;
import static org.junit.Assert.*;

public class HItemTypeSetTest {
    final Set<HItemTypeSet> unsafeSets = Sets.newHashSet(ZOMBIES, SKELETONS, FLYING_MOBS);

    @Test
    public void testSetContainsOtherSet() throws Exception {
        for (HItemTypeSet itemTypeSet1 : HItemTypeSet.values()) {
            for (HItemTypeSet itemTypeSet2 : HItemTypeSet.values()) {
                if (!itemTypeSet1.equals(itemTypeSet2)
                        && (!unsafeSets.contains(itemTypeSet2))
                        && (!itemTypeSet2.entityTypes.isEmpty())
                        && (!itemTypeSet2.subSets.contains(itemTypeSet1))
                        && (!itemTypeSet1.subSets.contains(itemTypeSet2))) {
                    assertFalse(String.format("A set '%s' contains all entity types of set '%s'",
                            itemTypeSet1, itemTypeSet2),
                            itemTypeSet1.entityTypes.containsAll(itemTypeSet2.entityTypes));
                }
            }
        }
    }

    @Test
    public void testSubSetContainsEntities() throws Exception {
        for(HItemTypeSet itemTypeSet : HItemTypeSet.values()) {
            for (HItemTypeSet subSet : itemTypeSet.subSets) {
                for (EntityType entityType : itemTypeSet.entityTypes) {
                    assertFalse(String.format("A sub-set '%s' of set '%s' contains entity type '%s'",
                            subSet, itemTypeSet, entityType),
                            subSet.entityTypes.contains(entityType));
                }
            }
        }
    }

    @Test
    public void testDuplicates() throws Exception {
        for(HItemTypeSet itemTypeSet1 : HItemTypeSet.values()) {
            if (!unsafeSets.contains(itemTypeSet1)) {
                for (HItemTypeSet itemTypeSet2 : HItemTypeSet.values()) {
                    if (!unsafeSets.contains(itemTypeSet2) && !itemTypeSet1.equals(itemTypeSet2)) {
                        for (EntityType entityType : itemTypeSet1.entityTypes) {
                            assertFalse(String.format("A set '%s' contains entity type '%s' of set '%s'",
                                    itemTypeSet2, entityType, itemTypeSet1),
                                    itemTypeSet2.entityTypes.contains(entityType));
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testAllEntityTypes() throws Exception {
        List<EntityType> aloneEntityTypes = Lists.newArrayList(
                PLAYER);
        List<EntityType> deadEntityTypes = Lists.newArrayList(
                ACACIA_BOAT, ACACIA_CHEST_BOAT, BAMBOO_RAFT, BAMBOO_CHEST_RAFT,
                AREA_EFFECT_CLOUD, ARMOR_STAND, ARROW, BIRCH_BOAT,
                BIRCH_CHEST_BOAT, BLOCK_DISPLAY, BREEZE_WIND_CHARGE, CHERRY_BOAT,
                CHERRY_CHEST_BOAT, CHEST_MINECART, COMMAND_BLOCK_MINECART, DARK_OAK_BOAT,
                DARK_OAK_CHEST_BOAT, DRAGON_FIREBALL, EGG, ENDER_PEARL,
                END_CRYSTAL, EXPERIENCE_BOTTLE, EXPERIENCE_ORB, EYE_OF_ENDER,
                FALLING_BLOCK, FIREBALL, FIREWORK_ROCKET, FISHING_BOBBER,
                FURNACE_MINECART, GLOW_ITEM_FRAME, HOPPER_MINECART, INTERACTION,
                ITEM, ITEM_DISPLAY, ITEM_FRAME, JUNGLE_BOAT,
                JUNGLE_CHEST_BOAT, LEASH_KNOT, LIGHTNING_BOLT, LLAMA_SPIT,
                MANGROVE_BOAT, MANGROVE_CHEST_BOAT, MARKER, MINECART,
                OAK_BOAT, OAK_CHEST_BOAT, OMINOUS_ITEM_SPAWNER, PAINTING,
                PALE_OAK_BOAT, PALE_OAK_CHEST_BOAT, POTION, SHULKER_BULLET,
                SMALL_FIREBALL, SNOWBALL, SPAWNER_MINECART, SPRUCE_BOAT,
                SPRUCE_CHEST_BOAT, SPECTRAL_ARROW, TEXT_DISPLAY, TNT,
                TNT_MINECART, TRIDENT, UNKNOWN, WIND_CHARGE,
                WITHER_SKULL);

        for(EntityType entityType : EntityType.values()) {
            boolean isFound = aloneEntityTypes.contains(entityType) || deadEntityTypes.contains(entityType);
            if (!isFound) {
                for (HItemTypeSet itemTypeSet : HItemTypeSet.values()) {
                    if (itemTypeSet.entityTypes.contains(entityType)) {
                        isFound = true;
                        break;
                    }
                }
            }
            assertTrue(String.format("Entity type '%s' is not found", entityType), isFound);
        }
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(ANIMALS.isContains(CHICKEN));
        assertFalse(ANIMALS.isContains(CREEPER));
    }

    @Test
    public void testView() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for (HItemTypeSet itemTypeSet : HItemTypeSet.values()) {
            stringBuilder.append(itemTypeSet);
            if(!itemTypeSet.subSets.isEmpty()) {
                stringBuilder.append(String.format(" (includes %s)", getSortedList(itemTypeSet.subSets)));
            }
            stringBuilder.append(String.format(": %s\n", getSortedList(itemTypeSet.entityTypes)));
        }
        assertEquals("ANIMALS: [ALLAY, ARMADILLO, AXOLOTL, BAT, BEE, CAMEL, CAT, CHICKEN, " +
                "COD, COW, DOLPHIN, DONKEY, FOX, FROG, GLOW_SQUID, GOAT, " +
                "HORSE, LLAMA, MOOSHROOM, MULE, OCELOT, PANDA, PARROT, PIG, " +
                "POLAR_BEAR, PUFFERFISH, RABBIT, SALMON, SHEEP, SNIFFER, SQUID, STRIDER, " +
                "TADPOLE, TROPICAL_FISH, TURTLE, WOLF, ZOMBIE_HORSE]\n" +
                "GOLEMS: [IRON_GOLEM, SNOW_GOLEM]\n" +
                "MONSTERS: [BLAZE, BOGGED, BREEZE, CAVE_SPIDER, CREAKING, CREAKING_TRANSIENT, CREEPER, DROWNED, " +
                "ELDER_GUARDIAN, ENDERMAN, ENDERMITE, ENDER_DRAGON, EVOKER, EVOKER_FANGS, GHAST, GIANT, " +
                "GUARDIAN, HOGLIN, HUSK, ILLUSIONER, MAGMA_CUBE, PHANTOM, PIGLIN, PIGLIN_BRUTE, " +
                "PILLAGER, RAVAGER, SHULKER, SILVERFISH, SKELETON, SKELETON_HORSE, SLIME, SPIDER, " +
                "STRAY, VEX, VINDICATOR, WARDEN, WITCH, WITHER, WITHER_SKELETON, ZOGLIN, " +
                "ZOMBIE, ZOMBIE_VILLAGER, ZOMBIFIED_PIGLIN]\n" +
                "CREATURES (includes [ANIMALS, GOLEMS, MONSTERS]): [PLAYER, TRADER_LLAMA, VILLAGER, WANDERING_TRADER]\n" +
                "ZOMBIES: [BOGGED, DROWNED, GIANT, HUSK, PIGLIN, PIGLIN_BRUTE, WITHER_SKELETON, ZOMBIE, " +
                "ZOMBIE_VILLAGER, ZOMBIFIED_PIGLIN]\n" +
                "SKELETONS: [SKELETON, STRAY, WITHER_SKELETON]\n" +
                "FLYING_MOBS: [ALLAY, BAT, BEE, BREEZE, ENDER_DRAGON, GHAST, PARROT, PHANTOM, " +
                "VEX, WITHER]\n", stringBuilder.toString());
    }

    private static <T> List<String> getSortedList(Set<T> list) {
        String[] strings = new String[list.size()];
        int i = 0;
        for (T item : list) {
            strings[i] = item.toString();
            i ++;
        }
        Arrays.sort(strings);

        return Lists.newArrayList(strings);
    }
}