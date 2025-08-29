package com.gmail.uprial.customcreatures.schema;

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
                        "    - CUSTOM",
                        "  exclude-reasons:",
                        "    - BREEDING",
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
                "reasons: [CUSTOM], exclude-reasons: [BREEDING], probability: 50, " +
                "probability-player-multiplier: {sort: CLOSEST, statistic: DAMAGE_DEALT, divider: 5000.0, max: 5.0}}", itemFilter.toString());
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
    public void testEmptyExcludeReasonsFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty exclude reasons of filter. Use default value NULL");
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
                        "    - CUSTOM"),
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
                        "    - CUSTOM",
                        "  exclude-reasons:",
                        "    - BREEDING"),
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
                        "    - CUSTOM",
                        "  exclude-reasons:",
                        "    - BREEDING",
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
                        "    - CUSTOM",
                        "  exclude-reasons:",
                        "    - BREEDING",
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
                "    - CUSTOM"), getCustomLogger(), "f", "filter");
        assertEquals(100, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.CUSTOM, ""));
        assertEquals(0, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.INFECTION, ""));
        assertEquals(0, getPasses(10, filter, EntityType.PIG, SpawnReason.CUSTOM, ""));
        assertEquals(0, getPasses(10, filter, EntityType.PIG, SpawnReason.INFECTION, ""));
    }

    @Test
    public void testPassTypesAndExcludeReasons() throws Exception {
        HItemFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  exclude-reasons:",
                "    - CUSTOM"), getCustomLogger(), "f", "filter");
        assertEquals(0, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.CUSTOM, ""));
        assertEquals(100, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.INFECTION, ""));
        assertEquals(0, getPasses(10, filter, EntityType.PIG, SpawnReason.CUSTOM, ""));
        assertEquals(0, getPasses(10, filter, EntityType.PIG, SpawnReason.INFECTION, ""));
    }

    @Test
    public void testPassTypesAndWorlds() throws Exception {
        HItemFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  worlds:",
                "    - world"), getCustomLogger(), "f", "filter");
        assertEquals(0, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.CUSTOM, ""));
        assertEquals(100, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.CUSTOM, "world"));
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