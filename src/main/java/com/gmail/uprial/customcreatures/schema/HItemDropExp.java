package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityDeathEvent;

import static com.gmail.uprial.customcreatures.common.Formatter.format;

public final class HItemDropExp {
    private final String title;
    private final IValue<Integer> dropExp;

    // https://minecraft.fandom.com/wiki/Experience
    private static final int MAX_EXP = 32767;

    private HItemDropExp(String title, IValue<Integer> dropExp) {
        this.title = title;
        this.dropExp = dropExp;
    }

    public void apply(CustomLogger customLogger, EntityDeathEvent event) {
        final int dropExpValue = dropExp.getValue();
        if (customLogger.isDebugMode()) {
            customLogger.debug(String.format("Handle %s: set drop exp of %s to %d",
                    title, format(event.getEntity()), dropExpValue));
        }
        event.setDroppedExp(dropExpValue);
    }

    public static HItemDropExp getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        IValue<Integer> exp = HValue.getIntFromConfig(config, customLogger, key, title, 1, MAX_EXP);
        if (exp == null) {
            return null;
        }

        return new HItemDropExp(title, exp);
    }

    public String toString() {
        return dropExp.toString();
    }
}
