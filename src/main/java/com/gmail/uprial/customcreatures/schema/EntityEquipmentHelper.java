package com.gmail.uprial.customcreatures.schema;

import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import static com.gmail.uprial.customcreatures.schema.BodyType.*;
import static com.gmail.uprial.customcreatures.schema.HandType.MAIN_HAND;
import static com.gmail.uprial.customcreatures.schema.HandType.OFF_HAND;

public class EntityEquipmentHelper {

    public static void setItem(EntityEquipment entityEquipment, BodyType bodyType, ItemStack itemStack) {
        if (bodyType == HELMET) {
            entityEquipment.setHelmet(itemStack);
        } else if (bodyType == BOOTS) {
            entityEquipment.setBoots(itemStack);
        } else if (bodyType == CHESTPLATE) {
            entityEquipment.setChestplate(itemStack);
        } else if (bodyType == LEGGINGS) {
            entityEquipment.setLeggings(itemStack);
        }
    }

    public static void setItem(EntityEquipment entityEquipment, HandType handType, ItemStack itemStack) throws OffHandIsNotSupportedException {
        if (areTwoHandsSupported(entityEquipment)) {
            if (handType == MAIN_HAND) {
                entityEquipment.setItemInMainHand(itemStack);
            } else if (handType == OFF_HAND) {
                entityEquipment.setItemInOffHand(itemStack);
            }
        } else {
            if (handType == MAIN_HAND) {
                //noinspection deprecation
                entityEquipment.setItemInHand(itemStack);
            }  else if (handType == OFF_HAND) {
                throw new OffHandIsNotSupportedException();
            }
        }
    }

    public static void setItemDropChance(EntityEquipment entityEquipment, BodyType bodyType, int dropChance) {
        if (bodyType == HELMET) {
            entityEquipment.setHelmetDropChance(dropChance);
        } else if (bodyType == BOOTS) {
            entityEquipment.setBootsDropChance(dropChance);
        } else if (bodyType == CHESTPLATE) {
            entityEquipment.setChestplateDropChance(dropChance);
        } else if (bodyType == LEGGINGS) {
            entityEquipment.setLeggingsDropChance(dropChance);
        }
    }

    public static void setItemDropChance(EntityEquipment entityEquipment, HandType handType, int dropChance) throws OffHandIsNotSupportedException {
        if (areTwoHandsSupported(entityEquipment)) {
            if (handType == MAIN_HAND) {
                entityEquipment.setItemInMainHandDropChance(dropChance);
            } else if (handType == OFF_HAND) {
                entityEquipment.setItemInOffHandDropChance(dropChance);
            }
        } else {
            if (handType == MAIN_HAND) {
                //noinspection deprecation
                entityEquipment.setItemInHandDropChance(dropChance);
            }  else if (handType == OFF_HAND) {
                throw new OffHandIsNotSupportedException();
            }
        }
    }

    private static boolean areTwoHandsSupported(EntityEquipment entityEquipment) {
        try {
            entityEquipment.getClass().getDeclaredMethod("setItemInMainHand");
            entityEquipment.getClass().getDeclaredMethod("setItemInOffHand");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}