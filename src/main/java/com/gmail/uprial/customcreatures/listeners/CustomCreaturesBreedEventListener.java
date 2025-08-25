package com.gmail.uprial.customcreatures.listeners;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.google.common.collect.ImmutableSet;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityBreedEvent;

import java.util.Random;
import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Formatter.format;

public class CustomCreaturesBreedEventListener extends AbstractCustomCreaturesEventListener {
    private final Random random = new Random();

    public CustomCreaturesBreedEventListener(CustomCreatures plugin, CustomLogger customLogger) {
        super(plugin, customLogger);
    }

    private static final Set<Attribute> SCALABLE_ATTRIBUTES = ImmutableSet.<Attribute>builder()
            .add(Attribute.ARMOR)
            .add(Attribute.FOLLOW_RANGE)
            .add(Attribute.KNOCKBACK_RESISTANCE)
            .add(Attribute.MOVEMENT_SPEED)
            .add(Attribute.SCALE)
            .build();

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityBreed(EntityBreedEvent event) {
        if(!event.isCancelled() && (event.getEntity() instanceof Wolf)) {
            final Wolf child = (Wolf)event.getEntity();
            final Wolf p1 = (Wolf)event.getMother();
            final Wolf p2 = (Wolf)event.getFather();

            final double scale = 0.9D + 0.2D * random.nextDouble();

            for(final Attribute attribute : SCALABLE_ATTRIBUTES) {
                child.getAttribute(attribute).setBaseValue(getAvg(
                        p1.getAttribute(attribute).getBaseValue(),
                        p2.getAttribute(attribute).getBaseValue(),
                        scale
                ));
            }

            customLogger.info(String.format("Bred %s from %s and %s",
                    DebugListener.getDeeperFormat(child),
                    DebugListener.getDeeperFormat(p1),
                    DebugListener.getDeeperFormat(p2)));
        }
    }

    private double getAvg(final double v1, final double v2,final double scale) {
        return (v1 + v2) / 2.0D * scale;
    }
}
