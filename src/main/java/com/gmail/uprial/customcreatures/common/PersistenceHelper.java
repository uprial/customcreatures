package com.gmail.uprial.customcreatures.common;

import com.gmail.uprial.customcreatures.CustomCreatures;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Set;

import static com.gmail.uprial.customcreatures.common.MetadataHelper.getMetadata;
import static com.gmail.uprial.customcreatures.common.MetadataHelper.setMetadata;

public class PersistenceHelper {
    private static String SCOREBOARD_TAGS_PREFIX = "ph_";

    public static void setPersistentMetadata(CustomCreatures plugin, LivingEntity entity, String key, Double value) {
        if(entity instanceof Player) {
            setMetadata(plugin, entity, key, value);
        } else {
            //
            entity.addScoreboardTag(getPersistentMetadataKeyPrefix(key));
            entity.addScoreboardTag(String.format("%s_%." + DoubleHelper.MAX_RIGHT_SIZE + "f",
                    getPersistentMetadataKeyPrefix(key), value));
        }
    }

    public static Double getPersistentMetadata(LivingEntity entity, String key) {
        if(entity instanceof Player) {
            return (Double)getMetadata(entity, key);
        } else {
            Set<String> scoreboardTags = entity.getScoreboardTags();
            if(scoreboardTags.contains(getPersistentMetadataKeyPrefix(key))) {
                String prefix = getPersistentMetadataKeyPrefix(key) + "_";
                for(String tag : scoreboardTags) {
                    if(tag.startsWith(prefix)) {
                        return Double.valueOf(tag.substring(prefix.length()));
                    }
                }
                return null;
            } else {
                return null;
            }
        }
    }

    private static String getPersistentMetadataKeyPrefix(String key) {
        return SCOREBOARD_TAGS_PREFIX + key;
    }
}
