package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.AbstractValueRandom.getDistributionTypeFromConfig;
import static com.gmail.uprial.customcreatures.schema.RandomDistributionType.EXP_UP;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

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
        e.expectMessage("Empty distribution of i of handler 'x'. Use default value NORMAL");

        getDistributionTypeFromConfig(getPreparedConfig("i: "), getDebugFearingCustomLogger(), "i", "i of handler", "x");
    }

    @Test
    public void testDistribution() throws Exception {
        assertEquals(EXP_UP,  getDistributionTypeFromConfig(getPreparedConfig("i: ", "  distribution: EXP_UP"),
                getDebugFearingCustomLogger(), "i", "i of handler", "x"));
    }
}