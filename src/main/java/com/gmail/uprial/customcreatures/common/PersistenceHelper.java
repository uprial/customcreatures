package com.gmail.uprial.customcreatures.common;

import com.gmail.uprial.customcreatures.CustomCreatures;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Set;

import static com.gmail.uprial.customcreatures.common.MetadataHelper.getMetadata;
import static com.gmail.uprial.customcreatures.common.MetadataHelper.setMetadata;

public class PersistenceHelper {
    private static String SCOREBOARD_TAGS_PREFIX = "ph_";
    private static String STRING_NULL = "CACHED_NULL";

    public static void setPersistentMetadata(CustomCreatures plugin, LivingEntity entity, String key, Double value) {
        if(entity instanceof Player) {
            setMetadata(plugin, entity, key, value);
        } else {
            /*
                Only player entities can have persistent metadata, and for other entities metadata can only be a cache.
                Two key are used for performance reasons: we use a set as a map.
             */
            entity.addScoreboardTag(getPersistentMetadataKeyPrefix(key));
            entity.addScoreboardTag(String.format("%s_%." + DoubleHelper.MAX_RIGHT_SIZE + "f",
                    getPersistentMetadataKeyPrefix(key), value));

            setMetadata(plugin, entity, key, value);
        }
    }

    public static Double getPersistentMetadata(CustomCreatures plugin, LivingEntity entity, String key) {
        if(entity instanceof Player) {
            return (Double)getMetadata(entity, key);
        } else {
            Object metadata = getMetadata(entity, key);
            if(metadata != null) {
                if(STRING_NULL.equals(metadata)) {
                    return null;
                } else {
                    return (Double)metadata;
                }
            }

            Double value = null;
            Set<String> scoreboardTags = entity.getScoreboardTags();
            if(scoreboardTags.contains(getPersistentMetadataKeyPrefix(key))) {
                String prefix = getPersistentMetadataKeyPrefix(key) + "_";
                for(String tag : scoreboardTags) {
                    if(tag.startsWith(prefix)) {
                        value = Double.valueOf(tag.substring(prefix.length()));
                        break;
                    }
                }
            }

            setMetadata(plugin, entity, key, (value == null) ? STRING_NULL : value);
            return value;
        }
    }

    private static String getPersistentMetadataKeyPrefix(String key) {
        return SCOREBOARD_TAGS_PREFIX + key;
    }
}
