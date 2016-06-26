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
import static com.gmail.uprial.customcreatures.schema.ValueRandom.getFromConfig;
import static junit.framework.Assert.*;

public class ValueRandomTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testIsValue() throws Exception {
        assertTrue(ValueRandom.is(getPreparedConfig(
                "v:",
                " type: random"), "v"));
    }

    @Test
    public void testNoTypeValue() throws Exception {
        assertFalse(ValueRandom.is(getPreparedConfig(
                "v:",
                " kk: vv"), "v"));
    }

    @Test
    public void testIsNotValue() throws Exception {
        assertFalse(ValueRandom.is(getPreparedConfig("v: 1"), "v"));
    }

    @Test
    public void testEmptyValue() throws Exception {
        assertFalse(ValueRandom.is(getPreparedConfig(""), "v"));
    }

    @Test
    public void testNoDistribution() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty distribution of i of handler 'x'. Use default value NORMAL");

        getFromConfig(getPreparedConfig("i: "), getDebugFearingCustomLogger(), "i", "i of handler", "x");
    }

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
        ValueRandom valueRandom = getFromConfig(getPreparedConfig(
                "i: ",
                " min: 0",
                " max: 1"), getParanoiacCustomLogger(), "i", "i of handler", "x");
        assertEquals(0.0, valueRandom.min);
        assertEquals(1.0, valueRandom.max);
        assertEquals(NORMAL, valueRandom.distributionType);
    }

    @Test
    public void testNormalDistribution() throws Exception {
        ValueRandom valueRandom = new ValueRandom(NORMAL, 10, 20);

        Map<Integer,Long> distribution = getDistribution(1000, valueRandom);
        for (Integer i = 10; i < 20; i++) {
            assertTrue(distribution.get(i) > 50);
        }
        assertFalse(distribution.containsKey(9));
        assertFalse(distribution.containsKey(21));
    }

    @Test
    public void testExpDownDistribution() throws Exception {
        ValueRandom valueRandom = new ValueRandom(EXP_DOWN, 10, 13);

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
        ValueRandom valueRandom = new ValueRandom(EXP_UP, 10, 13);

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

    private static Map<Integer,Long> getDistribution(int tries, ValueRandom valueRandom) {
        Map<Integer,Long> distribution = new HashMap<>();
        for (Integer i = 0; i < tries; i++) {
            Integer value = (int)Math.round(Math.floor(valueRandom.getValue()));
            Long prev = distribution.containsKey(value) ? distribution.get(value) : 0;
            distribution.put(value, prev + 1);
        }

        return distribution;
    }
}