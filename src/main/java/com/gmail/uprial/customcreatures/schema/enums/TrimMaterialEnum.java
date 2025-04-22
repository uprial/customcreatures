package com.gmail.uprial.customcreatures.schema.enums;

import org.bukkit.inventory.meta.trim.TrimMaterial;

public enum TrimMaterialEnum implements ITypedEnum<TrimMaterial> {
    QUARTZ(TrimMaterial.QUARTZ),
    IRON(TrimMaterial.IRON),
    NETHERITE(TrimMaterial.NETHERITE),
    REDSTONE(TrimMaterial.REDSTONE),
    COPPER(TrimMaterial.COPPER),
    GOLD(TrimMaterial.GOLD),
    EMERALD(TrimMaterial.EMERALD),
    DIAMOND(TrimMaterial.DIAMOND),
    LAPIS(TrimMaterial.LAPIS),
    AMETHYST(TrimMaterial.AMETHYST),
    RESIN(TrimMaterial.RESIN);

    private final TrimMaterial type;

    TrimMaterialEnum(TrimMaterial type) {
        this.type = type;
    }

    @Override
    public TrimMaterial getType() {
        return type;
    }
}
