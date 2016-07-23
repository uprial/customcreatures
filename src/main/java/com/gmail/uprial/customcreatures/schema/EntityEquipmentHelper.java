package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.schema.exceptions.MethodIsNotSupportedException;
import com.gmail.uprial.customcreatures.schema.exceptions.OperationIsNotSupportedException;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

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
public final class EntityEquipmentHelper {

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
                //noinspection ErrorNotRethrown,NestedTryStatement
                try {
                    entityEquipment.setItemInMainHand(itemStack);
                } catch (NoSuchMethodError ignored) {
                    //noinspection deprecation
                    entityEquipment.setItemInHand(itemStack);
                }
            } else if (handType == OFF_HAND) {
                //noinspection ErrorNotRethrown,NestedTryStatement
                try {
                    entityEquipment.setItemInOffHand(itemStack);
                } catch (NoSuchMethodError ignored) {
                    throw new MethodIsNotSupportedException("setItemInOffHand");
                }
            }
        } catch (UnsupportedOperationException ignored) {
            throw new OperationIsNotSupportedException();
        }
    }

    public static void setItemDropChance(EntityEquipment entityEquipment, ClothType clothType, int dropChance) throws OperationIsNotSupportedException {
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

    public static void setItemDropChance(EntityEquipment entityEquipment, HandType handType, int dropChance) throws MethodIsNotSupportedException, OperationIsNotSupportedException {
        try {
            if (handType == MAIN_HAND) {
                //noinspection ErrorNotRethrown,NestedTryStatement
                try {
                    entityEquipment.setItemInMainHandDropChance(dropChance);
                } catch (NoSuchMethodError ignored) {
                    //noinspection deprecation
                    entityEquipment.setItemInHandDropChance(dropChance);
                }
            } else if (handType == OFF_HAND) {
                //noinspection ErrorNotRethrown,NestedTryStatement
                try {
                    entityEquipment.setItemInOffHandDropChance(dropChance);
                } catch (NoSuchMethodError ignored) {
                    throw new MethodIsNotSupportedException("setItemInOffHandDropChance");
                }
            }
        } catch (UnsupportedOperationException ignored) {
            throw new OperationIsNotSupportedException();
        }
    }
}
