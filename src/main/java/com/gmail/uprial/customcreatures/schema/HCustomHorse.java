package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

import java.util.HashSet;
import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Formatter.format;
import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getEnumOrDefault;

public final class HCustomHorse implements ICustomEntity {
    private static final Set<EntityType> POSSIBLE_ENTITY_TYPES = new HashSet<EntityType>(){{ add(EntityType.HORSE); }};

    private final String title;
    private final Horse.Color color;
    private final Horse.Style style;
    private final IValue<Integer> maxDomestication;
    private final IValue<Double> jumpStrength;

    private HCustomHorse(String title, Horse.Color color, Horse.Style style,
                         IValue<Integer> maxDomestication, IValue<Double> jumpStrength) {
        this.title = title;
        this.color = color;
        this.style = style;
        this.maxDomestication = maxDomestication;
        this.jumpStrength = jumpStrength;
    }

    @Override
    public void apply(CustomLogger customLogger, Entity entity) {
        final Horse horse = (Horse)entity;
        applyColor(customLogger, horse);
        applyStyle(customLogger, horse);
        applyMaxDomestication(customLogger, horse);
        applyJumpStrength(customLogger, horse);
    }

    public void applyColor(CustomLogger customLogger, Horse horse) {
        if (color != null) {
            final Horse.Color oldValue = horse.getColor();
            horse.setColor(color);
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: set color of %s from %s to %s",
                        title, format(horse), oldValue, color));
            }
        }
    }

    public void applyStyle(CustomLogger customLogger, Horse horse) {
        if (style != null) {
            final Horse.Style oldValue = horse.getStyle();
            horse.setStyle(style);
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: set style of %s from %s to %s",
                        title, format(horse), oldValue, style));
            }
        }
    }

    public void applyMaxDomestication(CustomLogger customLogger, Horse horse) {
        if (maxDomestication != null) {
            final Integer oldValue = horse.getDomestication();
            final Integer newValue = maxDomestication.getValue();
            horse.setMaxDomestication(newValue);
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: set max. domestication of %s from %d to %d",
                        title, format(horse), oldValue, newValue));
            }
        }
    }

    public void applyJumpStrength(CustomLogger customLogger, Horse horse) {
        if (jumpStrength != null) {
            final Double oldValue = horse.getJumpStrength();
            final Double newValue = jumpStrength.getValue();
            horse.setJumpStrength(newValue);
            if (customLogger.isDebugMode()) {
                customLogger.debug(String.format("Handle %s modification: set jump strength of %s from %.2f to %.2f",
                        title, format(horse), oldValue, newValue));
            }
        }
    }

    @Override
    public Set<EntityType> getPossibleEntityTypes() {
        return POSSIBLE_ENTITY_TYPES;
    }

    public static HCustomHorse getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        Horse.Color color = getEnumOrDefault(Horse.Color.class, config, customLogger,
                joinPaths(key, "color"), String.format("color of %s", title), null);
        Horse.Style style = getEnumOrDefault(Horse.Style.class, config, customLogger,
                joinPaths(key, "style"), String.format("style of %s", title), null);

        /*
            https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/AbstractHorse.html#setMaxDomestication(int)
            Maximum domestication must be greater than zero.
         */
        IValue<Integer> maxDomestication = HValue.getIntFromConfig(config, customLogger, joinPaths(key, "max-domestication"),
                String.format("max. domestication of %s", title), 1, Integer.MAX_VALUE);
        /*
            https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/AbstractHorse.html#setJumpStrength(double)
            You cannot set a jump strength to a value below 0 or above 2.
         */
        IValue<Double> jumpStrength = HValue.getDoubleFromConfig(config, customLogger, joinPaths(key, "jump-strength"),
                String.format("jump strength of %s", title), 0.0D, 2.0D);

        if ((color == null)
                && (style == null)
                && (maxDomestication == null)
                && (jumpStrength == null)) {
            throw new InvalidConfigException(String.format("No modifications found of %s", title));
        }

        return new HCustomHorse(title, color, style, maxDomestication, jumpStrength);
    }

    public String toString() {
        return String.format("Horse{color: %s, style: %s, max-domestication: %s, jump-strength: %s}",
                color, style, maxDomestication, jumpStrength);
    }
}
