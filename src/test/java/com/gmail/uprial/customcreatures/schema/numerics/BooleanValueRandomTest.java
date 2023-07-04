package com.gmail.uprial.customcreatures.schema.numerics;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static com.gmail.uprial.customcreatures.schema.numerics.BooleanValueRandom.getFromConfig;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.*;
import static org.junit.Assert.*;

public class BooleanValueRandomTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testNormalValue() throws Exception {
        BooleanValueRandom valueRandom = getFromConfig(getPreparedConfig(
                "b: ",
                " type: normal"), getCustomLogger(), "b", "b");
        assertEquals("BooleanValueRandom{distribution: NORMAL}", valueRandom.toString());
    }

    @Test
    public void testNormalDistribution() throws Exception {
        final BooleanValueRandom valueRandom = new BooleanValueRandom(NORMAL);

        for(int i = 0; i < 1000; i++) {
            final Map<Boolean, Long> distribution = getDistribution(valueRandom);
            assertTrue(distribution.get(true) > 430);
            assertTrue(distribution.get(true) < 570);
            assertTrue(distribution.get(false) > 430);
            assertTrue(distribution.get(false) < 570);
        }
    }

    @Test
    public void testExpDownDistribution() throws Exception {
        final BooleanValueRandom valueRandom = new BooleanValueRandom(EXP_DOWN);

        for(int i = 0; i < 1000; i++) {
            final Map<Boolean, Long> distribution = getDistribution(valueRandom);
            assertTrue(distribution.get(true) > 700);
            assertTrue(distribution.get(true) < 840);
            assertTrue(distribution.get(false) > 170);
            assertTrue(distribution.get(false) < 300);
        }
    }

    @Test
    public void testExpUpDistribution() throws Exception {
        final BooleanValueRandom valueRandom = new BooleanValueRandom(EXP_UP);

        for(int i = 0; i < 1000; i++) {
            final Map<Boolean, Long> distribution = getDistribution(valueRandom);
            assertTrue(distribution.get(true) > 170);
            assertTrue(distribution.get(true) < 300);
            assertTrue(distribution.get(false) > 700);
            assertTrue(distribution.get(false) < 840);
        }
    }

    private static Map<Boolean,Long> getDistribution(BooleanValueRandom valueRandom) {
        final Map<Boolean,Long> distribution = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            final Boolean value = valueRandom.getValue();
            final long prev = distribution.containsKey(value) ? distribution.get(value) : 0;
            distribution.put(value, prev + 1);
        }

        return distribution;
    }
}