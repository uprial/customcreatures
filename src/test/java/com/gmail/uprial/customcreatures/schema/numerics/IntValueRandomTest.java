package com.gmail.uprial.customcreatures.schema.numerics;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static com.gmail.uprial.customcreatures.schema.numerics.IntValueRandom.getFromConfig;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.EXP_DOWN;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.EXP_UP;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.NORMAL;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class IntValueRandomTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testNoMin() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty minimum of i");

        getFromConfig(getPreparedConfig("i: "), getCustomLogger(), "i", "i", 0, 100);
    }

    @Test
    public void testNoMax() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty maximum of i");

        getFromConfig(getPreparedConfig(
                "i: ",
                " min: 0"), getCustomLogger(), "i", "i", 0, 100);
    }

    @Test
    public void testNormalValue() throws Exception {
        IntValueRandom valueRandom = getFromConfig(getPreparedConfig(
                "i: ",
                " min: 0",
                " max: 1"), getCustomLogger(), "i", "i", 0, 100);
        assertEquals("IntValueRandom[distribution: NORMAL, min: 0, max: 1]", valueRandom.toString());
    }

    @Test
    public void testNormalDistribution() throws Exception {
        IntValueRandom valueRandom = new IntValueRandom(NORMAL, 10, 20);

        Map<Integer,Long> distribution = getDistribution(valueRandom);
        for (Integer i = 10; i < 20; i++) {
            assertTrue(distribution.get(i) > 50);
        }
        assertFalse(distribution.containsKey(9));
        assertFalse(distribution.containsKey(21));
    }

    @Test
    public void testExpDownDistribution() throws Exception {
        IntValueRandom valueRandom = new IntValueRandom(EXP_DOWN, 10, 13);

        Map<Integer, Long> distribution = getDistribution(valueRandom);
        assertTrue(distribution.get(10) > 400);
        assertTrue(distribution.get(10) < 1000);
        assertTrue(distribution.get(11) > 150);
        assertTrue(distribution.get(11) < 500);
        assertTrue(distribution.get(12) > 100);
        assertTrue(distribution.get(12) < 250);
        assertTrue(distribution.get(13) > 50);
        assertTrue(distribution.get(13) < 150);
        assertFalse(distribution.containsKey(9));
        assertFalse(distribution.containsKey(14));
    }

    @Test
    public void testExpUpDistribution() throws Exception {
        IntValueRandom valueRandom = new IntValueRandom(EXP_UP, 10, 13);

        Map<Integer, Long> distribution = getDistribution(valueRandom);
        assertTrue(distribution.get(10) > 50);
        assertTrue(distribution.get(10) < 150);
        assertTrue(distribution.get(11) > 100);
        assertTrue(distribution.get(11) < 250);
        assertTrue(distribution.get(12) > 150);
        assertTrue(distribution.get(12) < 400);
        assertTrue(distribution.get(13) > 400);
        assertTrue(distribution.get(13) < 1000);
        assertFalse(distribution.containsKey(9));
        assertFalse(distribution.containsKey(14));
    }

    private static Map<Integer,Long> getDistribution(IntValueRandom valueRandom) {
        Map<Integer,Long> distribution = new HashMap<>();
        for (Integer i = 0; i < 1000; i++) {
            Integer value = valueRandom.getValue();
            Long prev = distribution.containsKey(value) ? distribution.get(value) : 0;
            distribution.put(value, prev + 1);
        }

        return distribution;
    }
}