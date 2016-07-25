package com.gmail.uprial.customcreatures.schema.tasks;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.schema.HItemInHand;
import org.bukkit.entity.LivingEntity;

public class HItemInHandTask extends HItemAbstractTask {
    private final HItemInHand tool;

    public HItemInHandTask(HItemInHand tool, CustomLogger customLogger, LivingEntity entity) {
        super(customLogger, entity);
        this.tool = tool;
    }

    @Override
    public void run() {
        tool.applyImmediately(customLogger, entity);
    }
}
