package com.gmail.uprial.customcreatures.schema.numerics;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.numerics.AbstractValueRandom.getDistributionTypeFromConfig;
import static com.gmail.uprial.customcreatures.schema.numerics.RandomDistributionType.EXP_UP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractValueRandomTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testIsValue() throws Exception {
        assertTrue(AbstractValueRandom.is(getPreparedConfig(
                "v:",
                " type: random"), "v"));
    }

    @Test
    public void testNoTypeValue() throws Exception {
        assertFalse(AbstractValueRandom.is(getPreparedConfig(
                "v:",
                " kk: vv"), "v"));
    }

    @Test
    public void testIsNotValue() throws Exception {
        assertFalse(AbstractValueRandom.is(getPreparedConfig("v: 1"), "v"));
    }

    @Test
    public void testEmptyValue() throws Exception {
        assertFalse(AbstractValueRandom.is(getPreparedConfig(""), "v"));
    }

    @Test
    public void testNoDistribution() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty distribution of i. Use default value NORMAL");

        getDistributionTypeFromConfig(getPreparedConfig("i: "), getDebugFearingCustomLogger(), "i", "i");
    }

    @Test
    public void testDistribution() throws Exception {
        assertEquals(EXP_UP,  getDistributionTypeFromConfig(getPreparedConfig("i: ", "  distribution: EXP_UP"),
                getParanoiacCustomLogger(), "i", "i"));
    }
}