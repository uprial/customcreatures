package com.gmail.uprial.customcreatures.listeners;

import com.gmail.uprial.customcreatures.CreaturesConfig;
import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.google.common.collect.ImmutableMap;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityBreedEvent;

import java.util.Map;

public class CustomCreaturesBreedEventListener extends AbstractCustomCreaturesEventListener {
    public CustomCreaturesBreedEventListener(final CustomCreatures plugin,
                                             final CustomLogger customLogger) {
        super(plugin, customLogger);
    }

    private static class BreedConfig {
        private final double base;
        private final double max;

        BreedConfig(final double base,
                    final double max) {
            this.base = base;
            this.max = max;
        }

        public double getBase() {
            return base;
        }

        public double getMax() {
            return max;
        }
    }

    private static final Map<Attribute, BreedConfig> SCALABLE_ATTRIBUTES = ImmutableMap.<Attribute, BreedConfig>builder()
            .put(Attribute.ARMOR, new BreedConfig(0.0D, 14.0D))
            .put(Attribute.FOLLOW_RANGE, new BreedConfig(16.0D, 80.0))
            .put(Attribute.KNOCKBACK_RESISTANCE, new BreedConfig(0.0D, 0.8D))
            .put(Attribute.MOVEMENT_SPEED, new BreedConfig(0.3D, 0.54D))
            .put(Attribute.SCALE, new BreedConfig(1.0D, 3.0D))
            .build();

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityBreed(EntityBreedEvent event) {
        if(!event.isCancelled() && (event.getEntity() instanceof Wolf)) {
            final CreaturesConfig creaturesConfig = plugin.getCreaturesConfig();
            // Don't try to handle an entity if there was error in loading of config.
            if (creaturesConfig == null) {

                final Wolf child = (Wolf) event.getEntity();
                final Wolf p1 = (Wolf) event.getMother();
                final Wolf p2 = (Wolf) event.getFather();

                final double scale = creaturesConfig.getWolfSelectionRandomizer().getValue();

                for (final Map.Entry<Attribute, BreedConfig> entry : SCALABLE_ATTRIBUTES.entrySet()) {
                    final Attribute attribute = entry.getKey();
                    child.getAttribute(attribute).setBaseValue(getAvg(
                            p1.getAttribute(attribute).getBaseValue(),
                            p2.getAttribute(attribute).getBaseValue(),
                            entry.getValue(), scale
                    ));
                }

                customLogger.info(String.format("Bred %s from %s, %s, and scale %.2f",
                        DebugListener.getDeeperFormat(child),
                        DebugListener.getDeeperFormat(p1),
                        DebugListener.getDeeperFormat(p2),
                        scale));
            }
        }
    }

    private double getAvg(final double v1, final double v2,
                          final BreedConfig breedConfig,
                          final double scale) {
        return Math.min(breedConfig.getMax(),
                breedConfig.getBase() + ((v1 + v2) / 2.0D - breedConfig.getBase()) * scale);
    }
}
