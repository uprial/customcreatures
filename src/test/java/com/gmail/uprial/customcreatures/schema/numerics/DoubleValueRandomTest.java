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
        assertEquals("DoubleValueRandom{distribution: NORMAL, min: 0.00, max: 1.00}", valueRandom.toString());
    }

    @Test
    public void testNormalValue_MaxLessThanMin() throws Exception {
        DoubleValueRandom valueRandom = getFromConfig(getPreparedConfig(
                "i: ",
                " min: 0",
                " max: -1"), getCustomLogger(), "i", "i", -1, 100);
        assertEquals("DoubleValueRandom{distribution: NORMAL, min: 0.00, max: -1.00}", valueRandom.toString());
    }

    @Test
    public void testNormalDistribution() {
        final DoubleValueRandom valueRandom = new DoubleValueRandom(NORMAL, 10.0, 20.0);

        for(int i = 0; i < 1000; i++) {
            final Map<Integer, Long> distribution = getDistribution(valueRandom);
            assertTrue(distribution.get(10) > 20);
            assertTrue(distribution.get(10) < 90);
            for (int j = 11; j < 20; j++) {
                assertTrue(distribution.get(j) > 1_000/10-50);
                assertTrue(distribution.get(j) < 1_000/10+60);
            }
            assertTrue(distribution.get(20) > 20);
            assertTrue(distribution.get(20) < 90);
            assertFalse(distribution.containsKey(9));
            assertFalse(distribution.containsKey(21));
        }
    }

    @Test
    public void testNormalDistribution_MaxLessThanMin() {
        final DoubleValueRandom valueRandom = new DoubleValueRandom(NORMAL, -10.0, -20.0);

        for(int i = 0; i < 1000; i++) {
            final Map<Integer, Long> distribution = getDistribution(valueRandom);
            assertTrue(distribution.get(-10) > 20);
            assertTrue(distribution.get(-10) < 90);
            for (int j = -11; j > -20; j--) {
                assertTrue(distribution.get(j) > 1_000/10-50);
                assertTrue(distribution.get(j) < 1_000/10+60);
            }
            assertTrue(distribution.get(-20) > 20);
            assertTrue(distribution.get(-20) < 90);
            assertFalse(distribution.containsKey(-9));
            assertFalse(distribution.containsKey(-21));
        }
    }

    @Test
    public void testExpDownDistribution() {
        final DoubleValueRandom valueRandom = new DoubleValueRandom(EXP_DOWN, 10.0, 13.0);

        for(int i = 0; i < 1000; i++) {
            final Map<Integer, Long> distribution = getDistribution(valueRandom);
            assertTrue(distribution.get(10) > 330);
            assertTrue(distribution.get(10) < 490);
            assertTrue(distribution.get(11) > 280);
            assertTrue(distribution.get(11) < 420);
            assertTrue(distribution.get(12) > 120);
            assertTrue(distribution.get(12) < 250);
            assertTrue(distribution.get(13) > 15);
            assertTrue(distribution.get(13) < 90);
            assertFalse(distribution.containsKey(9));
            assertFalse(distribution.containsKey(14));
        }
    }

    @Test
    public void testExpUpDistribution() {
        final DoubleValueRandom valueRandom = new DoubleValueRandom(EXP_UP, 10.0, 13.0);

        for(int i = 0; i < 1000; i++) {
            final Map<Integer, Long> distribution = getDistribution(valueRandom);
            assertTrue(distribution.get(10) > 15);
            assertTrue(distribution.get(10) < 90);
            assertTrue(distribution.get(11) > 120);
            assertTrue(distribution.get(11) < 250);
            assertTrue(distribution.get(12) > 280);
            assertTrue(distribution.get(12) < 420);
            assertTrue(distribution.get(13) > 330);
            assertTrue(distribution.get(13) < 490);
            assertFalse(distribution.containsKey(9));
            assertFalse(distribution.containsKey(14));
        }
    }

    private static Map<Integer,Long> getDistribution(DoubleValueRandom valueRandom) {
        final Map<Integer,Long> distribution = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            final Integer value = (int)Math.round(valueRandom.getValue());
            final long prev = distribution.containsKey(value) ? distribution.get(value) : 0;
            distribution.put(value, prev + 1);
        }

        return distribution;
    }
}