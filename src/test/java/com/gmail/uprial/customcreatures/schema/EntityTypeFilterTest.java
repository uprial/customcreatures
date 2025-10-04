package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.EntityTypeFilter.getFromConfig;
import static com.gmail.uprial.customcreatures.schema.Probability.MAX_PERCENT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EntityTypeFilterTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeFilter() throws Exception {
        EntityTypeFilter entityTypeFilter = getFromConfig(getPreparedConfig(
                "f:",
                        "  types:",
                        "    - COW",
                        "  exclude-types:",
                        "    - HUSK",
                        "  type-sets:",
                        "    - MONSTERS",
                        "  exclude-type-sets:",
                        "    - SKELETONS",
                        "  reasons:",
                        "    - NATURAL"),
                getParanoiacCustomLogger(), "f", "filter");
        assertEquals("{types: [COW], exclude-types: [HUSK]," +
                " type-sets: [MONSTERS], exclude-type-sets: [SKELETONS]}", entityTypeFilter.toString());
    }

    @Test
    public void testEmptyFilter() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty filter");
        getFromConfig(getPreparedConfig(
                "?:"),
                getParanoiacCustomLogger(), "f", "filter");
    }

    @Test
    public void testNoRestrictionsFilter() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No entity type restrictions found in filter");
        getFromConfig(getPreparedConfig(
                "f:",
                " k: v"),
                getCustomLogger(), "f", "filter");
    }

    @Test
    public void testEmptyTypesFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty types of filter. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "f:",
                "  x"),
                getDebugFearingCustomLogger(), "f", "filter");
    }

    @Test
    public void testEmptyExcludeTypesFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty exclude types of filter. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE"),
                getDebugFearingCustomLogger(), "f", "filter");
    }

    @Test
    public void testEmptyTypeSetsFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty type sets of filter. Use default value NULL");
        getFromConfig(getPreparedConfig(
                        "f:",
                        "  types:",
                        "    - ZOMBIE",
                        "  exclude-types:",
                        "    - SKELETON"),
                getDebugFearingCustomLogger(), "f", "filter");
    }

    @Test
    public void testEmptyExcludeTypeSetsFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty exclude type sets of filter. Use default value NULL");
        getFromConfig(getPreparedConfig(
                        "f:",
                        "  types:",
                        "    - ZOMBIE",
                        "  exclude-types:",
                        "    - SKELETON",
                        "  type-sets:",
                        "    - ANIMALS"),
                getDebugFearingCustomLogger(), "f", "filter");
    }

    @Test
    public void testCanNotExcludedNotIncludedFilter() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Can't exclude entity type SKELETON from [PLAYER] in filter");
        getFromConfig(getPreparedConfig(
                        "f:",
                        "  types:",
                        "    - PLAYER",
                        "  exclude-types:",
                        "    - SKELETON",
                        "  type-sets:",
                        "    - PLAYERS",
                        "  exclude-type-sets:",
                        "    - PLAYERS"),
                getDebugFearingCustomLogger(), "f", "filter");
    }

    @Test
    public void testEmptyIncludedFilter() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Excluded all entity types in filter");
        getFromConfig(getPreparedConfig(
                        "f:",
                        "  types:",
                        "    - ZOMBIE",
                        "  exclude-types:",
                        "    - ZOMBIE",
                        "  type-sets:",
                        "    - GOLEMS",
                        "  exclude-type-sets:",
                        "    - GOLEMS"),
                getDebugFearingCustomLogger(), "f", "filter");
    }

    @Test
    public void testPassTypeSetsAndExcludeTypes() throws Exception {
        EntityTypeFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  type-sets:",
                "    - ANIMALS",
                "  exclude-types:",
                "    - WOLF"), getCustomLogger(), "f", "filter");
        assertEquals(100, getPasses(10, filter, EntityType.COW));
        assertEquals(100, getPasses(10, filter, EntityType.SHEEP));
        assertEquals(0, getPasses(10, filter, EntityType.WOLF));
    }

    @Test
    public void testPassExcludeTypeSetsAndTypes() throws Exception {
        EntityTypeFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  exclude-type-sets:",
                "    - GOLEMS",
                "  types:",
                "    - WOLF",
                "    - COPPER_GOLEM",
                "    - IRON_GOLEM",
                "    - SNOW_GOLEM"), getCustomLogger(), "f", "filter");
        assertEquals(0, getPasses(10, filter, EntityType.COPPER_GOLEM));
        assertEquals(0, getPasses(10, filter, EntityType.IRON_GOLEM));
        assertEquals(0, getPasses(10, filter, EntityType.SNOW_GOLEM));
        assertEquals(100, getPasses(10, filter, EntityType.WOLF));
    }

    @Test
    public void testPassTypesAndTypeSets() throws Exception {
        EntityTypeFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  type-sets:",
                "    - ANIMALS"), getCustomLogger(), "f", "filter");
        assertEquals(100, getPasses(10, filter, EntityType.ZOMBIE));
        assertEquals(100, getPasses(10, filter, EntityType.CHICKEN));
        assertEquals(100, getPasses(10, filter, EntityType.COW));
        assertEquals(0, getPasses(10, filter, EntityType.IRON_GOLEM));
    }

    private static int getPasses(int tries, EntityTypeFilter filter, EntityType entityType) {
        final LivingEntity entity = mock(LivingEntity.class);
        when(entity.getType()).thenReturn(entityType);

        int passes = 0;
        for (int i = 0; i < tries; i++) {
            if (filter.isPassed(entity)) {
                passes += 1;
            }
        }

        return (int)Math.round(((double) passes * MAX_PERCENT) / tries);
    }
}