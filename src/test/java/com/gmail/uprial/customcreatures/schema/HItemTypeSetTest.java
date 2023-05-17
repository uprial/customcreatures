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
                DROPPED_ITEM, EXPERIENCE_ORB, LEASH_HITCH, PAINTING,
                ARROW, SNOWBALL, FIREBALL, SMALL_FIREBALL,
                ENDER_PEARL, ENDER_SIGNAL, THROWN_EXP_BOTTLE, ITEM_FRAME,
                WITHER_SKULL, PRIMED_TNT, FALLING_BLOCK, FIREWORK,
                SPECTRAL_ARROW, SHULKER_BULLET, DRAGON_FIREBALL,
                ARMOR_STAND, MINECART_COMMAND, BOAT, MINECART,
                MINECART_CHEST, MINECART_FURNACE, MINECART_TNT, MINECART_HOPPER,
                MINECART_MOB_SPAWNER, ENDER_CRYSTAL, SPLASH_POTION,
                AREA_EFFECT_CLOUD, EGG, FISHING_HOOK, LIGHTNING,
                LLAMA_SPIT, TRIDENT, GLOW_ITEM_FRAME, MARKER,
                CHEST_BOAT, UNKNOWN);

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
        assertEquals("ANIMALS: [ALLAY, AXOLOTL, BAT, BEE, CAT, CHICKEN, COD, COW, " +
                "DOLPHIN, DONKEY, FOX, FROG, GLOW_SQUID, GOAT, HORSE, LLAMA, " +
                "MULE, MUSHROOM_COW, OCELOT, PANDA, PARROT, PIG, POLAR_BEAR, PUFFERFISH, " +
                "RABBIT, SALMON, SHEEP, SQUID, STRIDER, TADPOLE, TROPICAL_FISH, TURTLE, " +
                "WOLF, ZOMBIE_HORSE]\n" +
                "GOLEMS: [IRON_GOLEM, SNOWMAN]\n" +
                "MONSTERS: [BLAZE, CAVE_SPIDER, CREEPER, DROWNED, ELDER_GUARDIAN, ENDERMAN, ENDERMITE, ENDER_DRAGON, " +
                "EVOKER, EVOKER_FANGS, GHAST, GIANT, GUARDIAN, HOGLIN, HUSK, ILLUSIONER, " +
                "MAGMA_CUBE, PHANTOM, PIGLIN, PIGLIN_BRUTE, PILLAGER, RAVAGER, SHULKER, SILVERFISH, " +
                "SKELETON, SKELETON_HORSE, SLIME, SPIDER, STRAY, VEX, VINDICATOR, WARDEN, " +
                "WITCH, WITHER, WITHER_SKELETON, ZOGLIN, ZOMBIE, ZOMBIE_VILLAGER, ZOMBIFIED_PIGLIN]\n" +
                "CREATURES (includes [ANIMALS, GOLEMS, MONSTERS]): [PLAYER, TRADER_LLAMA, VILLAGER, WANDERING_TRADER]\n" +
                "ZOMBIES: [DROWNED, GIANT, HUSK, PIGLIN, PIGLIN_BRUTE, WITHER_SKELETON, ZOMBIE, ZOMBIE_VILLAGER, " +
                "ZOMBIFIED_PIGLIN]\n" +
                "SKELETONS: [SKELETON, STRAY]\n" +
                "FLYING_MOBS: [ALLAY, BAT, BEE, ENDER_DRAGON, GHAST, PARROT, PHANTOM, VEX, WITHER]\n", stringBuilder.toString());
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