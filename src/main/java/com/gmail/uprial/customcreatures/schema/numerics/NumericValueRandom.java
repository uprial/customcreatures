package com.gmail.uprial.customcreatures.schema.numerics;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getEnum;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.NORMAL;

public abstract class NumericValueRandom<T> extends AbstractValueRandom<T> {
    final T min;
    final T max;

    NumericValueRandom(RandomDistributionType distributionType, T min, T max) {
        super(distributionType);
        this.min = min;
        this.max = max;
    }
}
