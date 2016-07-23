package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemFilter.getFromConfig;
import static com.gmail.uprial.customcreatures.schema.Probability.MAX_PERCENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HItemFilterTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeFilter() throws Exception {
        HItemFilter itemFilter = getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  reasons:",
                "    - NATURAL",
                "  probability: 50"),
                getParanoiacCustomLogger(), "f", "filter");
        assertEquals("[types: [ZOMBIE], reasons: [NATURAL], probability: 50]", itemFilter.toString());
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
                "  reasons:",
                "    - NATURAL",
                "  probability: 50"),
                getDebugFearingCustomLogger(), "f", "filter");
    }

    @Test
    public void testEmptyReasonsFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty reasons of filter. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  probability: 50"),
                getDebugFearingCustomLogger(), "f", "filter");
    }

    @Test
    public void testEmptyProbabilityFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty probability of filter. Use default value 100");
        getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  reasons:",
                "    - NATURAL"),
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
        assertEquals(100, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.NATURAL));
        assertEquals(0, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.INFECTION));
        assertEquals(0, getPasses(10, filter, EntityType.PIG, SpawnReason.NATURAL));
        assertEquals(0, getPasses(10, filter, EntityType.PIG, SpawnReason.INFECTION));
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
        assertTrue(getPasses(100, filter, EntityType.ZOMBIE, SpawnReason.NATURAL) > 20);
        assertTrue(getPasses(100, filter, EntityType.ZOMBIE, SpawnReason.NATURAL) < 80);
        assertEquals(0, getPasses(10, filter, EntityType.PIG, SpawnReason.NATURAL));
    }

    @Test
    public void testPassZeroProbability() throws Exception {
        HItemFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  probability: 0"), getCustomLogger(), "f", "filter");
        assertEquals(0, getPasses(10, filter, EntityType.ZOMBIE, SpawnReason.NATURAL));
    }

    private static int getPasses(int tries, HItemFilter filter, EntityType entityType, SpawnReason spawnReason) {
        int passes = 0;
        for (int i = 0; i < tries; i++) {
            if (filter.isPassed(entityType, spawnReason)) {
                passes += 1;
            }
        }

        return Math.round((passes * MAX_PERCENT) / tries);
    }
}