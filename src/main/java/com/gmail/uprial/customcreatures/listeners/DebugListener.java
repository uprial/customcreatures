package com.gmail.uprial.customcreatures.listeners;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;

import java.util.Map;

import static com.gmail.uprial.customcreatures.common.Formatter.format;

public class DebugListener implements Listener {
    private final CustomLogger customLogger;

    public DebugListener(final CustomLogger customLogger) {
        this.customLogger = customLogger;
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!event.isCancelled()
                && (event.getEntity() instanceof LivingEntity)
                && (event.getDamageSource().getCausingEntity() instanceof Player)) {

            customLogger.info(String.format("%s, Damage: %.2f/%.2f",
                    getDeeperFormat((LivingEntity) event.getEntity()),
                    event.getDamage(), event.getFinalDamage()));
        }
    }

    public static String getDeeperFormat(final LivingEntity entity) {
        final StringBuilder sb = new StringBuilder(format(entity));

        sb.append(String.format(", Health: %.2f/%.2f", entity.getHealth(), entity.getAttribute(Attribute.MAX_HEALTH).getBaseValue()));
        sb.append(String.format(", Armor: %.2f/%.2f", entity.getAttribute(Attribute.ARMOR).getBaseValue(), entity.getAttribute(Attribute.ARMOR).getValue()));

        sb.append(String.format(", Speed: %.2f", entity.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue()));
        sb.append(String.format(", Follow range: %.2f", entity.getAttribute(Attribute.FOLLOW_RANGE).getBaseValue()));
        sb.append(String.format(", Knockback resistance: %.2f", entity.getAttribute(Attribute.KNOCKBACK_RESISTANCE).getBaseValue()));
        sb.append(String.format(", Scale: %.2f", entity.getAttribute(Attribute.SCALE).getBaseValue()));

        final EntityEquipment e = entity.getEquipment();

        sb.append(", Helmet: " + f(e.getHelmet()));
        sb.append(", Chestplate: " + f(e.getChestplate()));
        sb.append(", Leggings: " + f(e.getLeggings()));
        sb.append(", Boots: " + f(e.getBoots()));
        sb.append(", MainHand: " + f(e.getItemInMainHand()));
        sb.append(", OffHand: " + f(e.getItemInOffHand()));
        sb.append(", Body: " + f(e.getItem(EquipmentSlot.BODY)));
        sb.append(", Saddle: " + f(e.getItem(EquipmentSlot.SADDLE)));

        for (final PotionEffect potionEffect : entity.getActivePotionEffects()) {
            sb.append(", " + potionEffect.getType().getName() + "-" + potionEffect.getAmplifier());
        }

        return sb.toString();
    }

    private static String f(final ItemStack itemStack) {
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
}
