package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static com.gmail.uprial.customcreatures.schema.IntValueRandom.getFromConfig;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.EXP_DOWN;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.EXP_UP;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.NORMAL;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class IntValueRandomTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testNoMin() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty minimum of i of handler 'x'");

        getFromConfig(getPreparedConfig("i: "), getParanoiacCustomLogger(), "i", "i of handler", "x");
    }

    @Test
    public void testNoMax() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty maximum of i of handler 'x'");

        getFromConfig(getPreparedConfig(
                "i: ",
                " min: 0"), getParanoiacCustomLogger(), "i", "i of handler", "x");
    }

    @Test
    public void testNormalValue() throws Exception {
        IntValueRandom valueRandom = getFromConfig(getPreparedConfig(
                "i: ",
                " min: 0",
                " max: 1"), getParanoiacCustomLogger(), "i", "i of handler", "x");
        assertEquals(0, valueRandom.min);
        assertEquals(1, valueRandom.max);
        assertEquals(NORMAL, valueRandom.distributionType);
    }

    @Test
    public void testNormalDistribution() throws Exception {
        IntValueRandom valueRandom = new IntValueRandom(NORMAL, 10, 20);

        Map<Integer,Long> distribution = getDistribution(1000, valueRandom);
        for (Integer i = 10; i < 20; i++) {
            assertTrue(distribution.get(i) > 50);
        }
        assertFalse(distribution.containsKey(9));
        assertFalse(distribution.containsKey(21));
    }

    @Test
    public void testExpDownDistribution() throws Exception {
        IntValueRandom valueRandom = new IntValueRandom(EXP_DOWN, 10, 13);

        Map<Integer,Long> distribution = getDistribution(1000, valueRandom);
        assertTrue(distribution.get(10) > 400);
        assertTrue(distribution.get(10) < 1000);
        assertTrue(distribution.get(11) > 200);
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

        Map<Integer,Long> distribution = getDistribution(1000, valueRandom);
        assertTrue(distribution.get(10) > 50);
        assertTrue(distribution.get(10) < 150);
        assertTrue(distribution.get(11) > 100);
        assertTrue(distribution.get(11) < 250);
        assertTrue(distribution.get(12) > 200);
        assertTrue(distribution.get(12) < 400);
        assertTrue(distribution.get(13) > 500);
        assertTrue(distribution.get(13) < 1000);
        assertFalse(distribution.containsKey(9));
        assertFalse(distribution.containsKey(14));
    }

    private static Map<Integer,Long> getDistribution(int tries, IntValueRandom valueRandom) {
        Map<Integer,Long> distribution = new HashMap<>();
        for (Integer i = 0; i < tries; i++) {
            Integer value = valueRandom.getValue();
            Long prev = distribution.containsKey(value) ? distribution.get(value) : 0;
            distribution.put(value, prev + 1);
        }

        return distribution;
    }
}