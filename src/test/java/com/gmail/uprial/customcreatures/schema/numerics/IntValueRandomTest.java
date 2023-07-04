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
        assertEquals("IntValueRandom{distribution: NORMAL, min: 0, max: 1}", valueRandom.toString());
    }

    @Test
    public void testNormalDistribution() throws Exception {
        final IntValueRandom valueRandom = new IntValueRandom(NORMAL, 10, 20);

        for(int i = 0; i < 100; i++) {
            final Map<Integer, Long> distribution = getDistribution(valueRandom);
            for (int j = 10; j <= 20; j++) {
                assertTrue(distribution.get(j) > 1_000/11-45);
                assertTrue(distribution.get(j) < 1_000/11+45);
            }
            assertFalse(distribution.containsKey(9));
            assertFalse(distribution.containsKey(21));
        }
    }

    @Test
    public void testExpDownDistribution() throws Exception {
        final IntValueRandom valueRandom = new IntValueRandom(EXP_DOWN, 10, 13);

        for(int i = 0; i < 1000; i++) {
            final Map<Integer, Long> distribution = getDistribution(valueRandom);
            assertTrue(distribution.get(10) > 460);
            assertTrue(distribution.get(10) < 600);
            assertTrue(distribution.get(11) > 180);
            assertTrue(distribution.get(11) < 330);
            assertTrue(distribution.get(12) > 90);
            assertTrue(distribution.get(12) < 200);
            assertTrue(distribution.get(13) > 50);
            assertTrue(distribution.get(13) < 130);
            assertFalse(distribution.containsKey(9));
            assertFalse(distribution.containsKey(14));
        }
    }

    @Test
    public void testExpUpDistribution() throws Exception {
        final IntValueRandom valueRandom = new IntValueRandom(EXP_UP, 10, 13);

        for(int i = 0; i < 1000; i++) {
            final Map<Integer, Long> distribution = getDistribution(valueRandom);
            assertTrue(distribution.get(10) > 50);
            assertTrue(distribution.get(10) < 130);
            assertTrue(distribution.get(11) > 90);
            assertTrue(distribution.get(11) < 200);
            assertTrue(distribution.get(12) > 180);
            assertTrue(distribution.get(12) < 330);
            assertTrue(distribution.get(13) > 460);
            assertTrue(distribution.get(13) < 600);
            assertFalse(distribution.containsKey(9));
            assertFalse(distribution.containsKey(14));
        }
    }

    @Test
    public void testExpDownDistribution_TwoValues() throws Exception {
        final IntValueRandom valueRandom = new IntValueRandom(EXP_DOWN, 1, 2);

        for(int i = 0; i < 1000; i++) {
            final Map<Integer, Long> distribution = getDistribution(valueRandom);
            assertTrue(distribution.get(1) > 700);
            assertTrue(distribution.get(1) < 840);
            assertTrue(distribution.get(2) > 170);
            assertTrue(distribution.get(2) < 300);
        }
    }

    private static Map<Integer,Long> getDistribution(IntValueRandom valueRandom) {
        final Map<Integer,Long> distribution = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            final Integer value = valueRandom.getValue();
            final long prev = distribution.containsKey(value) ? distribution.get(value) : 0;
            distribution.put(value, prev + 1);
        }

        return distribution;
    }
}