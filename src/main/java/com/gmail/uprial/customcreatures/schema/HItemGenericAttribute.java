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

    public Attribute getAttribute() {
        return attribute;
    }

    public String getTitle() {
        return title;
    }

    public double getHardMin() { return hardMin; }

    public double getHardMax() { return hardMax; }
}
