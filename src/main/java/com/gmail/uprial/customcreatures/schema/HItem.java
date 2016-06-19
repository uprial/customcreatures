package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

public class HItem {
    private final HItemFilter filter;
    private final Value maxSpeed;

    public HItem(HItemFilter filter, Value maxSpeed) {
        this.filter = filter;
        this.maxSpeed = maxSpeed;
    }

    public static HItem getFromConfig(FileConfiguration config, CustomLogger customLogger, String key) throws InvalidConfigException {
        HItemFilter filter = HItemFilter.getFromConfig(config, customLogger, key + ".filter", "filter of handler", key);
        Value maxSpeed = Value.getFromConfig(config, customLogger, key + ".max-speed", "maximum speed of handler", key);

        return new HItem(filter, maxSpeed);
    }
}
