package com.gmail.uprial.customcreatures.schema.numerics;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.EXP_DOWN;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.EXP_UP;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.NORMAL;
import static com.gmail.uprial.customcreatures.schema.numerics.DoubleValueRandom.getFromConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DoubleValueRandomTest extends TestConfigBase {
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
        DoubleValueRandom valueRandom = getFromConfig(getPreparedConfig(
                "i: ",
                " min: 0",
                " max: 1"), getCustomLogger(), "i", "i", 0, 100);
        assertEquals(0, valueRandom.min.intValue());
        assertEquals(1, valueRandom.max.intValue());
        assertEquals(NORMAL, valueRandom.distributionType);
        assertEquals("DoubleValueRandom[distribution: NORMAL, min: 0.00, max: 1.00]", valueRandom.toString());
    }

    @Test
    public void testNormalDistribution() throws Exception {
        DoubleValueRandom valueRandom = new DoubleValueRandom(NORMAL, 10.0, 20.0);

        Map<Integer,Long> distribution = getDistribution(valueRandom);
        for (Integer i = 10; i < 20; i++) {
            assertTrue(distribution.get(i) > 50);
        }
        assertFalse(distribution.containsKey(9));
        assertFalse(distribution.containsKey(21));
    }

    @Test
    public void testExpDownDistribution() throws Exception {
        DoubleValueRandom valueRandom = new DoubleValueRandom(EXP_DOWN, 10.0, 13.0);

        Map<Integer,Long> distribution = getDistribution(valueRandom);
        assertTrue(distribution.get(10) > 400);
        assertTrue(distribution.get(10) < 1000);
        assertTrue(distribution.get(11) > 200);
        assertTrue(distribution.get(11) < 500);
        assertTrue(distribution.get(12) > 100);
        assertTrue(distribution.get(12) < 250);
        assertFalse(distribution.containsKey(9));
        assertFalse(distribution.containsKey(13));
    }

    @Test
    public void testExpUpDistribution() throws Exception {
        DoubleValueRandom valueRandom = new DoubleValueRandom(EXP_UP, 10.0, 13.0);

        Map<Integer,Long> distribution = getDistribution(valueRandom);
        assertTrue(distribution.get(10) > 100);
        assertTrue(distribution.get(10) < 200);
        assertTrue(distribution.get(11) > 200);
        assertTrue(distribution.get(11) < 400);
        assertTrue(distribution.get(12) > 400);
        assertTrue(distribution.get(12) < 600);
        assertTrue(distribution.get(13) > 100);
        assertTrue(distribution.get(13) < 200);
        assertFalse(distribution.containsKey(9));
        assertFalse(distribution.containsKey(14));
    }

    private static Map<Integer,Long> getDistribution(DoubleValueRandom valueRandom) {
        Map<Integer,Long> distribution = new HashMap<>();
        for (Integer i = 0; i < 1000; i++) {
            Integer value = (int)Math.round(Math.floor(valueRandom.getValue()));
            Long prev = distribution.containsKey(value) ? distribution.get(value) : 0;
            distribution.put(value, prev + 1);
        }

        return distribution;
    }
}