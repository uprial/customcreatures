package com.gmail.uprial.customcreatures.common;

import com.gmail.uprial.customcreatures.CustomCreatures;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Set;

import static com.gmail.uprial.customcreatures.common.DoubleHelper.formatDoubleValue;
import static com.gmail.uprial.customcreatures.common.MetadataHelper.getMetadata;
import static com.gmail.uprial.customcreatures.common.MetadataHelper.setMetadata;

/*
    Only player metadata is persistent. Metadata of other entities can only be a runtime cache.

    Good news: the scoreboard tags are persistent for all the entities.
    Bad news: the scoreboard tags are a set but not a map.

    The original idea is described here: https://www.spigotmc.org/threads/persistent-metadata-on-entities.233213/.

    Two tags in the scoreboard are used for performance reasons:
        a) the first tag allows a quick check that there is a second tag,
        b) the second tag contains the value and can be found only via a full-scan of all the tags.
 */
public class PersistenceHelper {
    // Since we're in the global scope of scoreboard tags, the prefix makes our tags unique.
    private static String SCOREBOARD_TAGS_PREFIX = "ph_";

    /*
        Persistent data in the scoreboard is only needed for the server restarts. Then it's cached into metadata.

        The CACHED_NULL_VALUE allows to differentiate two cases:
            a) when there is no cache in metadata at all
            b) and when there is on data in the scoreboard but this absence needs to be cached too.
     */
    private static String CACHED_NULL_VALUE = "CACHED_NULL";

    private static String KV_DELIMITER = "_";

    public static void setPersistentMetadata(CustomCreatures plugin, LivingEntity entity, String key, Double value) {
        if(entity instanceof Player) {
            setMetadata(plugin, entity, key, value);
        } else {
            entity.addScoreboardTag(getPersistentMetadataKeyPrefix(key));
            entity.addScoreboardTag(getPersistentMetadataKeyPrefix(key) + KV_DELIMITER + "d" + formatDoubleValue(value));

            setMetadata(plugin, entity, key, value);
        }
    }

    public static Double getPersistentMetadata(CustomCreatures plugin, LivingEntity entity, String key) {
        if(entity instanceof Player) {
            return (Double)getMetadata(entity, key);
        } else {
            Object metadata = getMetadata(entity, key);
            if(metadata != null) {
                if(CACHED_NULL_VALUE.equals(metadata)) {
                    return null;
                } else {
                    return (Double)metadata;
                }
            }

            Double value = null;
            Set<String> scoreboardTags = entity.getScoreboardTags();
            if(scoreboardTags.contains(getPersistentMetadataKeyPrefix(key))) {
                String prefix = getPersistentMetadataKeyPrefix(key) + KV_DELIMITER;
                for(String tag : scoreboardTags) {
                    if(tag.startsWith(prefix)) {
                        value = Double.valueOf(tag.substring(prefix.length()));
                    }
                }
            }

            setMetadata(plugin, entity, key, (value == null) ? CACHED_NULL_VALUE : value);
            return value;
        }
    }

    private static String getPersistentMetadataKeyPrefix(String key) {
        return SCOREBOARD_TAGS_PREFIX + key;
    }
}
