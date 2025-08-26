package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.listeners.DebugListener;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import com.google.common.collect.ImmutableList;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.EntityBreedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gmail.uprial.customcreatures.common.DoubleHelper.formatDoubleValue;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;
import static com.gmail.uprial.customcreatures.config.ConfigReaderNumbers.getDouble;

public final class WolfSelection {
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
    private final IValue<Double> randomizer;
    private final Map<String,AttributeConfig> configs;

    private WolfSelection(final String title,
                          final IValue<Double> randomizer,
                          Map<String,AttributeConfig> configs) {
        this.title = title;
        this.randomizer = randomizer;
        this.configs = configs;
    }

    public void handle(CustomCreatures plugin, CustomLogger customLogger, EntityBreedEvent event) {
        final Wolf child = (Wolf) event.getEntity();
        final Wolf p1 = (Wolf) event.getMother();
        final Wolf p2 = (Wolf) event.getFather();

        final double scale = randomizer.getValue();

        for (final Map.Entry<String, AttributeConfig> entry : configs.entrySet()) {
            final Attribute attribute = getGA(entry.getKey()).getAttribute();
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

    private double getAvg(final double v1, final double v2,
                          final AttributeConfig attributeConfig,
                          final double scale) {
        return Math.min(attributeConfig.getMax(),
                attributeConfig.getBase() + ((v1 + v2) / 2.0D - attributeConfig.getBase()) * scale);
    }

    public static WolfSelection getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            customLogger.debug(String.format("Empty %s. Use default value NULL", title));
            return null;
        }

        final IValue<Double> randomizer = HValue.getDoubleFromConfig(config, customLogger, joinPaths(key, "randomizer"),
                String.format("randomizer of %s", title), 0.0D, 2.0D);
        if(randomizer == null) {
            throw new InvalidConfigException(String.format("Empty randomizer of %s", title));
        }

        final Map<String,AttributeConfig> configs = new HashMap<>();

        for (final String attribute : ATTRIBUTES) {
            final HItemGenericAttribute ga = getGA(attribute);
            configs.put(attribute, new AttributeConfig(
                    getDouble(config, customLogger,
                            joinPaths(key, joinPaths(attribute, "base")),
                            String.format("base %s of %s", attribute.replace("-", " "), title),
                            ga.getHardMin(), ga.getHardMax()),
                    getDouble(config, customLogger,
                            joinPaths(key, joinPaths(attribute, "max")),
                            String.format("max %s of %s", attribute.replace("-", " "), title),
                            ga.getHardMin(), ga.getHardMax())
            ));
        }

        return new WolfSelection(title, randomizer, configs);
    }

    public String toString() {
        final List<String> items = new ArrayList<>();
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
