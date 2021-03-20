package com.gmail.uprial.customcreatures.schema.tasks;

import com.gmail.uprial.customcreatures.CustomCreatures;
import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.schema.HItemInHand;
import org.bukkit.entity.LivingEntity;

public class HItemInHandTask extends HItemAbstractTask {
    private final HItemInHand tool;
    private final CustomCreatures plugin;

    public HItemInHandTask(HItemInHand tool, CustomCreatures plugin, CustomLogger customLogger, LivingEntity entity) {
        super(customLogger, entity);
        this.tool = tool;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if(entity.isValid()) {
            tool.applyImmediately(plugin, customLogger, entity);
        }
    }
}
