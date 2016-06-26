package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemFilter.getFromConfig;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

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
                getParanoiacCustomLogger(), "f", "filter of handler", "x");
        //noinspection ConstantConditions
        assertEquals("[types: [ZOMBIE], reasons: [NATURAL], probability: 50]", itemFilter.toString());
    }

    @Test
    public void testEmptyFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty filter of handler 'x'. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "?:"),
                getDebugFearingCustomLogger(), "f", "filter of handler", "x");
    }

    @Test
    public void testEmptyTypesFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty types of filter of handler 'x'. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "f:",
                "  reasons:",
                "    - NATURAL",
                "  probability: 50"),
                getDebugFearingCustomLogger(), "f", "filter of handler", "x");
    }

    @Test
    public void testEmptyReasonsFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty reasons of filter of handler 'x'. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  probability: 50"),
                getDebugFearingCustomLogger(), "f", "filter of handler", "x");
    }

    @Test
    public void testEmptyProbabilityFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty probability of filter of handler 'x'. Use default value 100");
        getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  reasons:",
                "    - NATURAL"),
                getDebugFearingCustomLogger(), "f", "filter of handler", "x");
    }

    @Test
    public void testPassTypesAndReasons() throws Exception {
        HItemFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  reasons:",
                "    - NATURAL"), getParanoiacCustomLogger(), "f", "filter of handler", "x");
        assertEquals(100, getPasses(10, filter, EntityType.ZOMBIE, CreatureSpawnEvent.SpawnReason.NATURAL));
        assertEquals(0, getPasses(10, filter, EntityType.ZOMBIE, CreatureSpawnEvent.SpawnReason.INFECTION));
        assertEquals(0, getPasses(10, filter, EntityType.PIG, CreatureSpawnEvent.SpawnReason.NATURAL));
        assertEquals(0, getPasses(10, filter, EntityType.PIG, CreatureSpawnEvent.SpawnReason.INFECTION));
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
                "  probability: 50"), getParanoiacCustomLogger(), "f", "filter of handler", "x");
        assertTrue(30 < getPasses(100, filter, EntityType.ZOMBIE, CreatureSpawnEvent.SpawnReason.NATURAL));
        assertTrue(70 > getPasses(100, filter, EntityType.ZOMBIE, CreatureSpawnEvent.SpawnReason.NATURAL));
        assertEquals(0, getPasses(10, filter, EntityType.PIG, CreatureSpawnEvent.SpawnReason.NATURAL));
    }

    @Test
    public void testPassZeroProbability() throws Exception {
        HItemFilter filter = getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  probability: 0"), getParanoiacCustomLogger(), "f", "filter of handler", "x");
        assertEquals(0, getPasses(10, filter, EntityType.ZOMBIE, CreatureSpawnEvent.SpawnReason.NATURAL));
    }

    private int getPasses(int tries, HItemFilter filter, EntityType entityType, CreatureSpawnEvent.SpawnReason spawnReason) {
        int passes = 0;
        for (int i = 0; i < tries; i++) {
            if (filter.pass(entityType, spawnReason)) {
                passes += 1;
            }
        }

        return Math.round(passes * 100 / tries);
    }
}