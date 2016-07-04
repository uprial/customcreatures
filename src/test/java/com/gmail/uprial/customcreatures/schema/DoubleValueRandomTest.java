package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.EXP_DOWN;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.EXP_UP;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.NORMAL;
import static com.gmail.uprial.customcreatures.schema.DoubleValueRandom.getFromConfig;
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

        getFromConfig(getPreparedConfig("i: "), getParanoiacCustomLogger(), "i", "i");
    }

    @Test
    public void testNoMax() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty maximum of i");

        getFromConfig(getPreparedConfig(
                "i: ",
                " min: 0"), getParanoiacCustomLogger(), "i", "i");
    }

    @Test
    public void testNormalValue() throws Exception {
        DoubleValueRandom valueRandom = getFromConfig(getPreparedConfig(
                "i: ",
                " min: 0",
                " max: 1"), getParanoiacCustomLogger(), "i", "i");
        assertEquals(0, valueRandom.min.intValue());
        assertEquals(1, valueRandom.max.intValue());
        assertEquals(NORMAL, valueRandom.distributionType);
    }

    @Test
    public void testNormalDistribution() throws Exception {
        DoubleValueRandom valueRandom = new DoubleValueRandom(NORMAL, 10.0, 20.0);

        Map<Integer,Long> distribution = getDistribution(1000, valueRandom);
        for (Integer i = 10; i < 20; i++) {
            assertTrue(distribution.get(i) > 50);
        }
        assertFalse(distribution.containsKey(9));
        assertFalse(distribution.containsKey(21));
    }

    @Test
    public void testExpDownDistribution() throws Exception {
        DoubleValueRandom valueRandom = new DoubleValueRandom(EXP_DOWN, 10.0, 13.0);

        Map<Integer,Long> distribution = getDistribution(1000, valueRandom);
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

        Map<Integer,Long> distribution = getDistribution(1000, valueRandom);
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

    private static Map<Integer,Long> getDistribution(int tries, DoubleValueRandom valueRandom) {
        Map<Integer,Long> distribution = new HashMap<>();
        for (Integer i = 0; i < tries; i++) {
            Integer value = (int)Math.round(Math.floor(valueRandom.getValue()));
            Long prev = distribution.containsKey(value) ? distribution.get(value) : 0;
            distribution.put(value, prev + 1);
        }

        return distribution;
    }
}