package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.schema.exceptions.MethodIsNotSupportedException;
import com.gmail.uprial.customcreatures.schema.exceptions.OperationIsNotSupportedException;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.function.Consumer;

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
    private static double DEFAULT_DROP_CHANCE = 0.085;

    /*
        https://minecraft.gamepedia.com/Drops
        The Looting enchantment increases this chance by 1% per level.
     */
    private static double DROP_CHANCE_PER_LOOTING_LEVEL = 0.01;

    private static String INITIAL_DROP_CHANCE_KEY = "initial-drop-chance";

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

    public static void setItemDropChance(CustomCreatures plugin, EntityEquipment entityEquipment, ClothType clothType, float dropChance) throws OperationIsNotSupportedException {
        try {
            //noinspection IfStatementWithTooManyBranches
            if (clothType == HELMET) {
                entityEquipment.setHelmet(setInitialDropChance(plugin, entityEquipment.getHelmet(), dropChance));
                entityEquipment.setHelmetDropChance(dropChance);
            } else if (clothType == BOOTS) {
                entityEquipment.setBoots(setInitialDropChance(plugin, entityEquipment.getBoots(), dropChance));
                entityEquipment.setBootsDropChance(dropChance);
            } else if (clothType == CHESTPLATE) {
                entityEquipment.setChestplate(setInitialDropChance(plugin, entityEquipment.getChestplate(), dropChance));
                entityEquipment.setChestplateDropChance(dropChance);
            } else if (clothType == LEGGINGS) {
                entityEquipment.setLeggings(setInitialDropChance(plugin, entityEquipment.getLeggings(), dropChance));
                entityEquipment.setLeggingsDropChance(dropChance);
            }
        } catch (UnsupportedOperationException ignored) {
            throw new OperationIsNotSupportedException();
        }
    }

    public static void setItemDropChance(CustomCreatures plugin, EntityEquipment entityEquipment, HandType handType, float dropChance) throws MethodIsNotSupportedException, OperationIsNotSupportedException {
        try {
            if (handType == MAIN_HAND) {
                entityEquipment.setItemInMainHand(setInitialDropChance(plugin, entityEquipment.getItemInMainHand(), dropChance));
                entityEquipment.setItemInMainHandDropChance(dropChance);
            } else if (handType == OFF_HAND) {
                entityEquipment.setItemInOffHand(setInitialDropChance(plugin, entityEquipment.getItemInOffHand(), dropChance));
                entityEquipment.setItemInOffHandDropChance(dropChance);
            }
        } catch (UnsupportedOperationException ignored) {
            throw new OperationIsNotSupportedException();
        }
    }

    public static double getDefaultDropChance() {
        return DEFAULT_DROP_CHANCE;
    }

    public static void handleLootBonusMobs(CustomCreatures plugin, EntityEquipment entityEquipment, int lootBonusMobs) {
        if(lootBonusMobs > 0) {
            handleLootBonusMobs(plugin, entityEquipment.getHelmet(), entityEquipment::setHelmetDropChance, lootBonusMobs);
            handleLootBonusMobs(plugin, entityEquipment.getBoots(), entityEquipment::setBootsDropChance, lootBonusMobs);
            handleLootBonusMobs(plugin, entityEquipment.getChestplate(), entityEquipment::setChestplateDropChance, lootBonusMobs);
            handleLootBonusMobs(plugin, entityEquipment.getLeggings(), entityEquipment::setLeggingsDropChance, lootBonusMobs);
            handleLootBonusMobs(plugin, entityEquipment.getItemInMainHand(), entityEquipment::setItemInMainHandDropChance, lootBonusMobs);
            handleLootBonusMobs(plugin, entityEquipment.getItemInOffHand(), entityEquipment::setItemInOffHandDropChance, lootBonusMobs);
        }
    }

    private static void handleLootBonusMobs(CustomCreatures plugin, ItemStack itemStack, Consumer<Float> dropChanceSetter, int lootBonusMobs) {
        if(itemStack != null) {
            Float initialDropChance = getInitialDropChance(plugin, itemStack);
            if(initialDropChance != null) {
                dropChanceSetter.accept(initialDropChance + (float)(DROP_CHANCE_PER_LOOTING_LEVEL * lootBonusMobs));
            }
        }

    }

    private static ItemStack setInitialDropChance(CustomCreatures plugin, ItemStack itemStack, float dropChance) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta != null) {
            itemMeta.getPersistentDataContainer().set(getInitialDropChanceKey(plugin), PersistentDataType.FLOAT, dropChance);
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    private static Float getInitialDropChance(CustomCreatures plugin, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta != null) {
            return itemMeta.getPersistentDataContainer().get(getInitialDropChanceKey(plugin), PersistentDataType.FLOAT);
        } else {
            return null;
        }
    }

    private static NamespacedKey getInitialDropChanceKey(CustomCreatures plugin) {
        return new NamespacedKey(plugin, INITIAL_DROP_CHANCE_KEY);
    }
}
