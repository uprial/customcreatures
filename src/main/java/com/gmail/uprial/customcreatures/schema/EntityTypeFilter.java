package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.HashSet;
import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getSet;

public class EntityTypeFilter {
    protected final Set<EntityType> entityTypes;
    protected final Set<EntityType> excludeEntityTypes;
    protected final Set<HItemTypeSet> entityTypeSets;
    protected final Set<HItemTypeSet> excludeEntityTypeSets;

    private final Set<EntityType> possibleEntityTypes;

    protected EntityTypeFilter(final EntityTypeFilter filter) {
        this(filter.entityTypes, filter.excludeEntityTypes,
                filter.entityTypeSets, filter.excludeEntityTypeSets);
    }

    private EntityTypeFilter(final Set<EntityType> entityTypes,
                             final Set<EntityType> excludeEntityTypes,
                             final Set<HItemTypeSet> entityTypeSets,
                             final Set<HItemTypeSet> excludeEntityTypeSets) {
        this.entityTypes = entityTypes;
        this.excludeEntityTypes = excludeEntityTypes;
        this.entityTypeSets = entityTypeSets;
        this.excludeEntityTypeSets = excludeEntityTypeSets;

        possibleEntityTypes = new HashSet<>();
        possibleEntityTypes.addAll(getEntityTypes(entityTypes, entityTypeSets));
        possibleEntityTypes.removeAll(getEntityTypes(excludeEntityTypes, excludeEntityTypeSets));
    }

    public boolean isPassed(final LivingEntity entity) {
        return getPossibleEntityTypes().contains(entity.getType());
        /*boolean typeFound = false;
        if (entityTypes != null) {
            if (entityTypes.contains(entity.getType())) {
                typeFound = true;
            }
        }
        if ((!typeFound) && (entityTypeSets != null)) {
            for(HItemTypeSet typeSet : entityTypeSets) {
                if(typeSet.contains(entity.getType())) {
                    typeFound = true;
                    break;
                }
            }
        }
        if(!typeFound) {
            return false;
        }

        if (excludeEntityTypes != null) {
            if (excludeEntityTypes.contains(entity.getType())) {
                typeFound = false;
            }
        }
        if ((typeFound) && (excludeEntityTypeSets != null)) {
            for(HItemTypeSet typeSet : excludeEntityTypeSets) {
                if(typeSet.contains(entity.getType())) {
                    typeFound = false;
                    break;
                }
            }
        }

        return typeFound;*/
    }

    public Set<EntityType> getPossibleEntityTypes() {
        return possibleEntityTypes;
    }

    private static Set<EntityType> getEntityTypes(final Set<EntityType> entityTypes,
                                                  final Set<HItemTypeSet> entityTypeSets) {

        final Set<EntityType> result = new HashSet<>();
        if (entityTypes != null) {
            result.addAll(entityTypes);
        }
        if (entityTypeSets != null) {
            for (HItemTypeSet typeSet : entityTypeSets) {
                result.addAll(typeSet.getAllEntityTypes());
            }
        }

        return result;
    }

    public static EntityTypeFilter getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        Set<EntityType> entityTypes = getSet(EntityType.class, config, customLogger,
                joinPaths(key, "types"), String.format("types of %s", title));
        Set<EntityType> excludeEntityTypes = getSet(EntityType.class, config, customLogger,
                joinPaths(key, "exclude-types"), String.format("exclude types of %s", title));
        Set<HItemTypeSet> entityTypeSets = getSet(HItemTypeSet.class, config, customLogger,
                joinPaths(key, "type-sets"), String.format("type sets of %s", title));
        Set<HItemTypeSet> excludeEntityTypeSets = getSet(HItemTypeSet.class, config, customLogger,
                joinPaths(key, "exclude-type-sets"), String.format("exclude type sets of %s", title));

        final Set<EntityType> included = getEntityTypes(entityTypes, entityTypeSets);
        final Set<EntityType> excluded = getEntityTypes(excludeEntityTypes, excludeEntityTypeSets);
        for (final EntityType entityType : excluded) {
            if(!included.contains(entityType)) {
                throw new InvalidConfigException(String.format("Can't exclude entity type %s from %s in %s",
                        entityType, included, title));
            }
        }
        if((entityTypes != null) || (excludeEntityTypes != null)
                || (entityTypeSets != null) || (excludeEntityTypeSets != null)) {
            included.removeAll(excluded);
            if(included.isEmpty()) {
                throw new InvalidConfigException(String.format("Excluded all entity types in %s", title));
            }
        }

        if ((entityTypes == null) && (excludeEntityTypes == null)
                && (entityTypeSets == null) && (excludeEntityTypeSets == null)) {
            throw new InvalidConfigException(String.format("No entity type restrictions found in %s", title));
        }

        return new EntityTypeFilter(entityTypes, excludeEntityTypes,
                entityTypeSets, excludeEntityTypeSets);
    }

    public String toString() {
        return String.format("{types: %s, exclude-types: %s," +
                        " type-sets: %s, exclude-type-sets: %s}",
                entityTypes, excludeEntityTypes,
                entityTypeSets, excludeEntityTypeSets);
    }
}
