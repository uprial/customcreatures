package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.schema.exceptions.MethodIsNotSupportedException;
import com.gmail.uprial.customcreatures.schema.exceptions.OperationIsNotSupportedException;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import static com.gmail.uprial.customcreatures.common.DoubleHelper.MIN_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.schema.ClothType.*;
import static com.gmail.uprial.customcreatures.schema.HandType.MAIN_HAND;
import static com.gmail.uprial.customcreatures.schema.HandType.OFF_HAND;

/*
 Test cases:
  - All 4 body types and items in two hands
  - Different entity types: Creeper, Zombie, Human
  - Both MineCraft versions
  - Set item and its drop chance
 */

@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
final public class EntityEquipmentHelper {
    /*
        https://minecraft.gamepedia.com/Drops
        When killed by a player or a tamed wolf, a monster can drop equipment and armor that it spawned with.
        Each piece of equipment the monster was spawned with is dropped with an 8.5% chance.
     */
    private static final double DEFAULT_DROP_CHANCE = 0.085D;

    public static void setItem(EntityEquipment entityEquipment, ClothType clothType, ItemStack itemStack) throws OperationIsNotSupportedException {
        try {
            //noinspection IfStatementWithTooManyBranches
            if (clothType == HELMET) {
                entityEquipment.setHelmet(itemStack);
            } else if (clothType == BOOTS) {
                entityEquipment.setBoots(itemStack);
            } else if (clothType == CHESTPLATE) {
                entityEquipment.setChestplate(itemStack);
            } else if (clothType == LEGGINGS) {
                entityEquipment.setLeggings(itemStack);
            }
        } catch (UnsupportedOperationException ignored) {
            throw new OperationIsNotSupportedException();
        }
    }

    public static void setItem(EntityEquipment entityEquipment, HandType handType, ItemStack itemStack) throws MethodIsNotSupportedException, OperationIsNotSupportedException {
        try {
            if (handType == MAIN_HAND) {
                entityEquipment.setItemInMainHand(itemStack);
            } else if (handType == OFF_HAND) {
                entityEquipment.setItemInOffHand(itemStack);
            }
        } catch (UnsupportedOperationException ignored) {
            throw new OperationIsNotSupportedException();
        }
    }

    public static void setItemDropChance(EntityEquipment entityEquipment, ClothType clothType, float dropChance) throws OperationIsNotSupportedException {
        try {
            //noinspection IfStatementWithTooManyBranches
            if (clothType == HELMET) {
                entityEquipment.setHelmetDropChance(dropChance);
            } else if (clothType == BOOTS) {
                entityEquipment.setBootsDropChance(dropChance);
            } else if (clothType == CHESTPLATE) {
                entityEquipment.setChestplateDropChance(dropChance);
            } else if (clothType == LEGGINGS) {
                entityEquipment.setLeggingsDropChance(dropChance);
            }
        } catch (UnsupportedOperationException ignored) {
            throw new OperationIsNotSupportedException();
        }
    }

    public static void setItemDropChance(EntityEquipment entityEquipment, HandType handType, float dropChance) throws MethodIsNotSupportedException, OperationIsNotSupportedException {
        try {
            if (handType == MAIN_HAND) {
                entityEquipment.setItemInMainHandDropChance(dropChance);
            } else if (handType == OFF_HAND) {
                entityEquipment.setItemInOffHandDropChance(dropChance);
            }
        } catch (UnsupportedOperationException ignored) {
            throw new OperationIsNotSupportedException();
        }
    }

    public static double getDefaultDropChance() {
        return DEFAULT_DROP_CHANCE;
    }

    public static boolean isDropChanceNotEmpty(final float dropChance) {
        return dropChance >= MIN_DOUBLE_VALUE / 10.D;
    }
}
