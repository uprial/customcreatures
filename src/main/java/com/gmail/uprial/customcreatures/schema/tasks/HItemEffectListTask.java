package com.gmail.uprial.customcreatures.schema.tasks;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.schema.HItemEffectsList;
import org.bukkit.entity.LivingEntity;

public class HItemEffectListTask extends HItemAbstractTask {
    private final HItemEffectsList effects;

    public HItemEffectListTask(HItemEffectsList effects, CustomLogger customLogger, LivingEntity entity) {
        super(customLogger, entity);
        this.effects = effects;
    }

    @Override
    public void run() {
        if (entity.isValid()) {
            effects.applyImmediately(customLogger, entity);
        }
    }
}
