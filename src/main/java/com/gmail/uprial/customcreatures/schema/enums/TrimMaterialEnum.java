package com.gmail.uprial.customcreatures.schema.enums;

import org.bukkit.inventory.meta.trim.TrimMaterial;

public final class TrimMaterialEnum {
    public static Class<? extends Enum> getClassName() {
        try {
            TrimMaterial.class.getField("RESIN");
            return TrimMaterialEnum_1_21_4.class;
        } catch (NoSuchFieldException ignored) {
            return TrimMaterialEnum_1_21_3.class;
        }
    }

    public static ITrimMaterial[] values() {
        try {
            TrimMaterial.class.getField("RESIN");
            return TrimMaterialEnum_1_21_4.values();
        } catch (NoSuchFieldException ignored) {
            return TrimMaterialEnum_1_21_3.values();
        }
    }
}
