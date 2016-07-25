package com.gmail.uprial.customcreatures.schema.tasks;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.entity.LivingEntity;

abstract class HItemAbstractTask implements Runnable {
    final CustomLogger customLogger;
    final LivingEntity entity;

    HItemAbstractTask(CustomLogger customLogger, LivingEntity entity) {
        this.customLogger = customLogger;
        this.entity = entity;
    }

    @Override
    public abstract void run();
}
