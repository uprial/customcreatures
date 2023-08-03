package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Set;

@SuppressWarnings("MarkerInterface")
interface ICustomEntity {
    Set<EntityType> getPossibleEntityTypes();

    void apply(CustomLogger customLogger, Entity entity);

}
