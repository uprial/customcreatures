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

            final LivingEntity entity = (LivingEntity)event.getEntity();

            final StringBuilder sb = new StringBuilder(format(entity));

            sb.append(String.format(", Health: %.2f/%.2f", entity.getHealth(), entity.getAttribute(Attribute.MAX_HEALTH).getBaseValue()));
            sb.append(String.format(", Armor: %.2f", entity.getAttribute(Attribute.ARMOR).getBaseValue()));
            sb.append(String.format(", Damage: %.2f/%.2f", event.getDamage(), event.getFinalDamage()));

            final EntityEquipment e = entity.getEquipment();

            sb.append(", Helmet: " + f(e.getHelmet()));
            sb.append(", Chestplate: " + f(e.getChestplate()));
            sb.append(", Leggings: " + f(e.getLeggings()));
            sb.append(", Boots: " + f(e.getBoots()));
            sb.append(", MainHand: " + f(e.getItemInMainHand()));
            sb.append(", OffHand: " + f(e.getItemInOffHand()));

            for (final PotionEffect potionEffect : entity.getActivePotionEffects()) {
                sb.append(", " + potionEffect.getType().getName() + "-" + potionEffect.getAmplifier());
            }

            customLogger.info(sb.toString());
        }
    }

    private static String f(final ItemStack itemStack) {
        if(itemStack == null) {
            return "null";
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
