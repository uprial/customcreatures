package com.gmail.uprial.customcreatures.common;

import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.function.Function;

import static com.gmail.uprial.customcreatures.common.MetadataHelper.getMetadata;
import static com.gmail.uprial.customcreatures.common.MetadataHelper.setMetadata;

/*
    Metadata of entities can only be a runtime cache.

    Good news: the scoreboard tags are persistent for all the entities.
    Bad news: the scoreboard tags are a set but not a map.

    The original idea is described here: https://www.spigotmc.org/threads/persistent-metadata-on-entities.233213/.

    Two tags in the scoreboard are used for performance reasons:
        a) the first tag allows a quick check that there is a second tag,
        b) the second tag contains the value and can be found only via a full-scan of all the tags.
 */
public class PersistenceHelper {
    // Since we're in the global scope of scoreboard tags, the prefix makes our tags unique.
    private static final String SCOREBOARD_TAGS_PREFIX = "ph_";

    /*
        Persistent data in the scoreboard is only needed for the server restarts. Then it's cached into metadata.

        The CACHED_NULL_VALUE allows to differentiate two cases:
            a) when there is no cache in metadata at all
            b) and when there is on data in the scoreboard but this absence needs to be cached too.
     */
    private static final String CACHED_NULL_VALUE = "CACHED_NULL";

    private static final String KV_DELIMITER = "_";

    public static void setDoublePersistentMetadata(JavaPlugin plugin, LivingEntity entity, String key, Double value) {
        setPersistentMetadata(plugin, entity, key, value, DoubleHelper::formatDoubleValue);
    }

    public static void setIntegerPersistentMetadata(JavaPlugin plugin, LivingEntity entity, String key, Integer value) {
        setPersistentMetadata(plugin, entity, key, value, String::valueOf);
    }

    public static Double getDoublePersistentMetadata(JavaPlugin plugin, LivingEntity entity, String key) {
        return getPersistentMetadata(plugin, entity, key, Double::valueOf);
    }

    public static Integer getIntegerPersistentMetadata(JavaPlugin plugin, LivingEntity entity, String key) {
        return getPersistentMetadata(plugin, entity, key, Integer::valueOf);
    }

    public static void addPersistentMetadataFlag(JavaPlugin plugin, LivingEntity entity, String key) {
        entity.addScoreboardTag(getPersistentMetadataKeyPrefix(key));
        setMetadata(plugin, entity, key, true);
    }

    public static boolean containsPersistentMetadataFlag(JavaPlugin plugin, LivingEntity entity, String key) {
        final Object metadata = getMetadata(entity, key);
        if(metadata != null) {
            return (Boolean)metadata;
        } else {
            final boolean value = entity.getScoreboardTags()
                    .contains(getPersistentMetadataKeyPrefix(key));

            setMetadata(plugin, entity, key, value);
            return value;
        }
    }

    private static <T> void setPersistentMetadata(JavaPlugin plugin, LivingEntity entity, String key, T value, Function<T,String> encoder) {
        Set<String> scoreboardTags = entity.getScoreboardTags();
        if(scoreboardTags.contains(getPersistentMetadataKeyPrefix(key))) {
            // Remove previous tags
            scoreboardTags.removeIf(
                    tag -> tag.startsWith(getPersistentMetadataKeyPrefix(key) + KV_DELIMITER));
        } else {
            entity.addScoreboardTag(getPersistentMetadataKeyPrefix(key));
        }

        entity.addScoreboardTag(getPersistentMetadataKeyPrefix(key) + KV_DELIMITER + encoder.apply(value));

        setMetadata(plugin, entity, key, value);
    }

    private static <T> T getPersistentMetadata(JavaPlugin plugin, LivingEntity entity, String key, Function<String,T> decoder) {
        Object metadata = getMetadata(entity, key);
        if(metadata != null) {
            if(CACHED_NULL_VALUE.equals(metadata)) {
                return null;
            } else {
                //noinspection unchecked
                return (T)metadata;
            }
        }

        T value = null;
        Set<String> scoreboardTags = entity.getScoreboardTags();
        if(scoreboardTags.contains(getPersistentMetadataKeyPrefix(key))) {
            String prefix = getPersistentMetadataKeyPrefix(key) + KV_DELIMITER;
            for(String tag : scoreboardTags) {
                if(tag.startsWith(prefix)) {
                    value = decoder.apply(tag.substring(prefix.length()));
                    break;
                }
            }
        }

        setMetadata(plugin, entity, key, (value == null) ? CACHED_NULL_VALUE : value);
        return value;
    }

    private static String getPersistentMetadataKeyPrefix(String key) {
        return SCOREBOARD_TAGS_PREFIX + key;
    }
}
