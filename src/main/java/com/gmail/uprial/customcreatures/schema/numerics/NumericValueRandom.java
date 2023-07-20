package com.gmail.uprial.customcreatures.schema.numerics;

public abstract class NumericValueRandom<T> extends AbstractValueRandom<T> {
    final T min;
    final T max;

    NumericValueRandom(RandomDistributionType distributionType, T min, T max) {
        super(distributionType);
        this.min = min;
        this.max = max;
    }
}