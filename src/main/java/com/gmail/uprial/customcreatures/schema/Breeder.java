package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import com.google.common.collect.ImmutableList;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityBreedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gmail.uprial.customcreatures.common.DoubleHelper.formatDoubleValue;
import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;
import static com.gmail.uprial.customcreatures.config.ConfigReaderNumbers.getDouble;

public final class Breeder {
    private static final List<String> ATTRIBUTES = ImmutableList.<String>builder()
            .add("base-armor")
            .add("follow-range")
            .add("knockback-resistance")
            .add("movement-speed")
            .add("scale")
            .build();

    private static class AttributeConfig {
        private final double base;
        private final double max;

        AttributeConfig(final double base,
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

        @Override
        public String toString() {
            return String.format("{base: %s, max: %s}", formatDoubleValue(base), formatDoubleValue(max));
        }
    }

    private final String title;
    private final EntityTypeFilter filter;
    private final IValue<Double> randomizer;
    private final Map<String,AttributeConfig> configs;

    private Breeder(final String title,
                    final EntityTypeFilter filter,
                    final IValue<Double> randomizer,
                    Map<String,AttributeConfig> configs) {
        this.title = title;
        this.filter = filter;
        this.randomizer = randomizer;
        this.configs = configs;
    }

    public boolean handleBreed(CustomLogger customLogger, EntityBreedEvent event) {
        if (filter.isPassed(event.getEntity())) {
            final LivingEntity child = event.getEntity();
            final LivingEntity p1 = event.getMother();
            final LivingEntity p2 = event.getFather();

            final double scale = randomizer.getValue();

            for (final Map.Entry<String, AttributeConfig> entry : configs.entrySet()) {
                final Attribute attribute = getGA(entry.getKey()).getAttribute();
                child.getAttribute(attribute).setBaseValue(getAvg(
                        p1.getAttribute(attribute).getBaseValue(),
                        p2.getAttribute(attribute).getBaseValue(),
                        entry.getValue(), scale
                ));
            }

            if(customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s: bred %s from %s, %s, and scale %.2f",
                        title, format(child), format(p1), format(p2), scale));
            }

            return true;
        } else {
            return false;
        }
    }

    private double getAvg(final double v1, final double v2,
                          final AttributeConfig attributeConfig,
                          final double scale) {
        return Math.min(attributeConfig.getMax(),
                attributeConfig.getBase() + ((v1 + v2) / 2.0D - attributeConfig.getBase()) * scale);
    }

    public static Breeder getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        final EntityTypeFilter filter = EntityTypeFilter.getFromConfig(config, customLogger, joinPaths(key, "filter"),
                String.format("filter of %s", title));

        final IValue<Double> randomizer = HValue.getDoubleFromConfig(config, customLogger, joinPaths(key, "randomizer"),
                String.format("randomizer of %s", title), 0.0D, 2.0D);
        if(randomizer == null) {
            throw new InvalidConfigException(String.format("Empty randomizer of %s", title));
        }

        final Map<String,AttributeConfig> configs = new HashMap<>();

        for (final String attribute : ATTRIBUTES) {
            final String attributeKey = joinPaths(key, attribute);
            final String attributeTitle = String.format("%s of %s", attribute.replace("-", " "), title);
            if (config.get(attributeKey) == null) {
                customLogger.debug(String.format("Empty %s. Use default value NULL", attributeTitle));
            } else {
                final HItemGenericAttribute ga = getGA(attribute);
                configs.put(attribute, new AttributeConfig(
                        getDouble(config, customLogger,
                                joinPaths(attributeKey, "base"),
                                String.format("base %s", attributeTitle),
                                ga.getHardMin(), ga.getHardMax()),
                        getDouble(config, customLogger,
                                joinPaths(attributeKey, "max"),
                                String.format("max %s", attributeTitle),
                                ga.getHardMin(), ga.getHardMax())
                ));
            }
        }

        return new Breeder(title, filter, randomizer, configs);
    }

    public String toString() {
        final List<String> items = new ArrayList<>();
        items.add(String.format("filter: %s", filter));
        items.add(String.format("randomizer: %s", randomizer));

        for (final String attribute : ATTRIBUTES) {
            items.add(String.format("%s: %s", attribute, configs.get(attribute)));
        }

        // Though a list is being joined, it's a set of attributes
        return String.format("{%s}", joinStrings(", ", items));
    }

    private static HItemGenericAttribute getGA(final String attribute) {
        return HItemAttributes.KEY_2_GENERIC_ATTRIBUTE.get(attribute);
    }
}
