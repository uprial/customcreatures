package com.gmail.uprial.customcreatures.common;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;

public final class Formatter {
    /*
        Except for one error case per
        HItemAttributes, HItemEnchantment, and HItemEntitySpecificAttributes,
        this function is used in debug mode only.
     */
    public static String format(final Entity entity) {
        if(entity == null) {
            return "null";
        }
        final Location location = entity.getLocation();
        final StringBuilder sb = new StringBuilder(String.format(
                "%s[%s:%.0f:%.0f:%.0f]#%s",
                entity.getType(),
                location.getWorld().getName(),
                location.getX(), location.getY(), location.getZ(),
                entity.getUniqueId().toString().substring(0, 8)));

        if(entity instanceof LivingEntity) {
            final LivingEntity le = (LivingEntity)entity;

            sb.append(String.format("{Health: %.2f/%.2f",
                    le.getHealth(), le.getAttribute(Attribute.MAX_HEALTH).getBaseValue()));
            sb.append(String.format(", Armor: %.2f/%.2f",
                    le.getAttribute(Attribute.ARMOR).getBaseValue(), le.getAttribute(Attribute.ARMOR).getValue()));
            sb.append(String.format(", Speed: %.2f",
                    le.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue()));

            // Doesn't exist for ARMOR_STAND
            if(le.getAttribute(Attribute.FOLLOW_RANGE) != null) {
                sb.append(String.format(", Follow range: %.2f",
                        le.getAttribute(Attribute.FOLLOW_RANGE).getBaseValue()));
            }
            sb.append(String.format(", Knockback resistance: %.2f",
                    le.getAttribute(Attribute.KNOCKBACK_RESISTANCE).getBaseValue()));
            sb.append(String.format(", Scale: %.2f",
                    le.getAttribute(Attribute.SCALE).getBaseValue()));

            final EntityEquipment e = le.getEquipment();

            final Map<String, ItemStack> slots = new HashMap<>();
            slots.put("Helmet", e.getHelmet());
            slots.put("Chestplate", e.getChestplate());
            slots.put("Leggings", e.getLeggings());
            slots.put("Boots", e.getBoots());
            slots.put("MainHand", e.getItemInMainHand());
            slots.put("OffHand", e.getItemInOffHand());
            slots.put("Body", e.getItem(EquipmentSlot.BODY));
            try {
                slots.put("Saddle", e.getItem(EquipmentSlot.SADDLE));
            } catch (NoSuchFieldError ignored) {
                // BC for versions 1.21.3...1.21.5
                if(le instanceof AbstractHorse) {
                    slots.put("Saddle", ((AbstractHorse)le).getInventory().getSaddle());
                }
            }
            for(final Map.Entry<String, ItemStack> entry : slots.entrySet()) {
                if((entry.getValue() != null) && (!entry.getValue().getType().isAir())) {
                    sb.append(String.format(", %s: %s", entry.getKey(), format(entry.getValue())));
                }
            }

            if(!le.getActivePotionEffects().isEmpty()) {
                for (final PotionEffect potionEffect : le.getActivePotionEffects()) {
                    sb.append(", " + format(potionEffect));
                }
            }

            sb.append("}");
        }

        return sb.toString();
    }

    private static String format(final ItemStack itemStack) {
        if(itemStack == null) {
            return "null";
        } else if (itemStack.getType().isAir()) {
            return "empty";
        }

        final StringBuilder sb = new StringBuilder(itemStack.getType().toString());
        if(itemStack.getMaxStackSize() > 1) {
            sb.append(String.format("x%d",
                    itemStack.getAmount()));
        }
        if(itemStack.getType().getMaxDurability() > 0) {
            sb.append(String.format(":%d/%d",
                    ((Damageable)itemStack.getItemMeta()).getDamage(),
                    itemStack.getType().getMaxDurability()));
        }
        for(final Map.Entry<Enchantment,Integer> entry : itemStack.getEnchantments().entrySet()) {
            sb.append(String.format("+%s-%d", entry.getKey().getName(), entry.getValue()));
        }
        return sb.toString();
    }

    public static String format(final PotionEffect effect) {
        if(effect == null) {
            return "null";
        }
        return String.format("%s{strength:%d, duration:%d}",
                effect.getType().getName(), effect.getAmplifier(), effect.getDuration());
    }
}
