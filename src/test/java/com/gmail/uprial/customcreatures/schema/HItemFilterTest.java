package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemFilter.getFromConfig;
import static com.gmail.uprial.customcreatures.schema.Probability.MAX_PERCENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HItemFilterTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeFilter() throws Exception {
        HItemFilter itemFilter = getFromConfig(getPreparedConfig(
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
                        "    - NATURAL",
                        "  worlds:",
                        "    - world",
                        "  probability: 50",
                        "  probability-player-multiplier:",
                        "    sort: closest",
                        "    statistic: DAMAGE_DEALT",
                        "    divider: 5_000",
                        "    max: 5.0"),
                getParanoiacCustomLogger(), "f", "filter");
        assertEquals("{types: [COW], exclude-types: [HUSK]," +
                " type-sets: [MONSTERS], exclude-type-sets: [SKELETONS], " +
                "reasons: [NATURAL], probability: 50, " +
                "probability-player-multiplier: {sort: CLOSEST, statistic: DAMAGE_DEALT, divider: 5000.0, max: 5.0}}", itemFilter.toString());
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
        e.expectMessage("No restrictions found in filter");
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
        e.expectMessage("Can't exclude SKELETON from [PLAYER] in filter");
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
    public void testEmptyReasonsFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty reasons of filter. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "f:",
                        "  types:",
                        "    - COW",
                        "  exclude-types:",
                        "    - HUSK",
                        "  type-sets:",
                        "    - MONSTERS",
                        "  exclude-type-sets:",
                        "    - SKELETONS"),
                getDebugFearingCustomLogger(), "f", "filter");
    }

    @Test
    public void testEmptyWorldsFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty worlds of filter. Use default value NULL");
        getFromConfig(getPreparedConfig(
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
                getDebugFearingCustomLogger(), "f", "filter");
    }

    @Test
    public void testEmptyProbabilityFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty probability of filter. Use default value 100");
        getFromConfig(getPreparedConfig(
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
                    "    - NATURAL",
                    "  worlds:",
                    "    - world"),
                getDebugFearingCustomLogger(), "f", "filter");
    }

    @Test
    public void testEmptyProbabilityPlayerMultiplier() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty probability player multiplier of filter. Use default value NULL");
        getFromConfig(getPreparedConfig(
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
                        "    - NATURAL",
                        "  worlds:",
                        "    - world",
                        "  probability: 100"),
                getDebugFearingCustomLogger(), "f", "filter");
    }

    @Test
    public void testPassTypesAndReasons() throws Exception {
        HItemFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  reasons:",
                "    - NATURAL"), getCustomLogger(), "f", "filter");
        assertEquals(100, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.NATURAL, ""));
        assertEquals(0, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.INFECTION, ""));
        assertEquals(0, getPasses(10, filter, EntityType.PIG, SpawnReason.NATURAL, ""));
        assertEquals(0, getPasses(10, filter, EntityType.PIG, SpawnReason.INFECTION, ""));
    }

    @Test
    public void testPassTypeSetsAndExcludeTypes() throws Exception {
        HItemFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  type-sets:",
                "    - ANIMALS",
                "  exclude-types:",
                "    - WOLF"), getCustomLogger(), "f", "filter");
        assertEquals(100, getPasses(10, filter, EntityType.COW, SpawnReason.NATURAL, ""));
        assertEquals(100, getPasses(10, filter, EntityType.SHEEP, SpawnReason.NATURAL, ""));
        assertEquals(0, getPasses(10, filter, EntityType.WOLF, SpawnReason.NATURAL, ""));
    }

    @Test
    public void testPassExcludeTypeSetsAndTypes() throws Exception {
        HItemFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  exclude-type-sets:",
                "    - GOLEMS",
                "  types:",
                "    - WOLF",
                "    - IRON_GOLEM",
                "    - SNOW_GOLEM"), getCustomLogger(), "f", "filter");
        assertEquals(0, getPasses(10, filter, EntityType.IRON_GOLEM, SpawnReason.NATURAL, ""));
        assertEquals(0, getPasses(10, filter, EntityType.SNOW_GOLEM, SpawnReason.NATURAL, ""));
        assertEquals(100, getPasses(10, filter, EntityType.WOLF, SpawnReason.NATURAL, ""));
    }

    @Test
    public void testPassTypesAndTypeSets() throws Exception {
        HItemFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  type-sets:",
                "    - ANIMALS"), getCustomLogger(), "f", "filter");
        assertEquals(100, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.NATURAL, ""));
        assertEquals(100, getPasses(10, filter, EntityType.CHICKEN, SpawnReason.NATURAL, ""));
        assertEquals(100, getPasses(10, filter, EntityType.COW, SpawnReason.NATURAL, ""));
        assertEquals(0, getPasses(10, filter, EntityType.IRON_GOLEM, SpawnReason.NATURAL, ""));
    }

    @Test
    public void testPassTypesAndWorlds() throws Exception {
        HItemFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  worlds:",
                "    - world"), getCustomLogger(), "f", "filter");
        assertEquals(0, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.NATURAL, ""));
        assertEquals(100, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.NATURAL, "world"));
    }

    @Test
    public void testPassTypesAndProbability() throws Exception {
        /*
            Probability that after n experiments there will be k successful results:

            Pn(k) = C^k.n ⋅ p^k ⋅ q^n−k, q=1−p
            Cn(k) = n! / (k! ⋅ (n - k)!)

            Let:
            n = 100
            k = 50
            p = 50% = 0.5
            q = 1 - 0.5 = 0.5

            Then:
            P100(50) = 100! / 50! / 50! * 0.5^50 * 0.5^50 = 8%
         */
        HItemFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  probability: 50"), getCustomLogger(), "f", "filter");
        assertTrue(getPasses(100, filter, EntityType.ZOMBIE, SpawnReason.NATURAL, "") > 20);
        assertTrue(getPasses(100, filter, EntityType.ZOMBIE, SpawnReason.NATURAL, "") < 80);
        assertEquals(0, getPasses(10, filter, EntityType.PIG, SpawnReason.NATURAL, ""));
    }

    @Test
    public void testPassZeroProbability() throws Exception {
        HItemFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  probability: 1"), getCustomLogger(), "f", "filter");
        assertTrue(getPasses(1000, filter, EntityType.ZOMBIE, SpawnReason.NATURAL, "") < 10);
    }

    private static int getPasses(int tries, HItemFilter filter, EntityType entityType, SpawnReason spawnReason, String world) {
        final LivingEntity entity = mock(LivingEntity.class);
        when(entity.getType()).thenReturn(entityType);

        int passes = 0;
        for (int i = 0; i < tries; i++) {
            if (filter.isPassed(entity, spawnReason, world)) {
                passes += 1;
            }
        }

        return (int)Math.round(((double) passes * MAX_PERCENT) / tries);
    }
}