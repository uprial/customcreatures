package com.gmail.uprial.customcreatures.schema;

import org.bukkit.attribute.Attribute;

class HItemGenericAttribute {
    private final Attribute attribute;
    private final String title;
    private final double hardMin;
    private final double hardMax;

    HItemGenericAttribute(Attribute attribute, String title, double hardMin, double hardMax) {
        this.attribute = attribute;
        this.title = title;
        this.hardMin = hardMin;
        this.hardMax = hardMax;
    }

    Attribute getAttribute() {
        return attribute;
    }

    String getTitle() {
        return title;
    }

    double getHardMin() { return hardMin; }

    double getHardMax() { return hardMax; }
}
