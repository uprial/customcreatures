package com.gmail.uprial.customcreatures.schema.enums;

import org.bukkit.inventory.meta.trim.TrimPattern;

public enum TrimPatternEnum implements ITypedEnum<TrimPattern> {
    SENTRY(TrimPattern.SENTRY),
    DUNE(TrimPattern.DUNE),
    COAST(TrimPattern.COAST),
    WILD(TrimPattern.WILD),
    WARD(TrimPattern.WARD),
    EYE(TrimPattern.EYE),
    VEX(TrimPattern.VEX),
    TIDE(TrimPattern.TIDE),
    SNOUT(TrimPattern.SNOUT),
    RIB(TrimPattern.RIB),
    SPIRE(TrimPattern.SPIRE),
    WAYFINDER(TrimPattern.WAYFINDER),
    SHAPER(TrimPattern.SHAPER),
    SILENCE(TrimPattern.SILENCE),
    RAISER(TrimPattern.RAISER),
    HOST(TrimPattern.HOST),
    FLOW(TrimPattern.FLOW),
    BOLT(TrimPattern.BOLT);

    private final TrimPattern type;

    TrimPatternEnum(TrimPattern type) {
        this.type = type;
    }

    @Override
    public TrimPattern getType() {
        return type;
    }
}
