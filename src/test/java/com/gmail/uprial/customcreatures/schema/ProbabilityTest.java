package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.Probability.getFromConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProbabilityTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeProbability() throws Exception {
        Probability probability = getFromConfig(getPreparedConfig(
                "p: 1"),
                getParanoiacCustomLogger(), "p", "probability");
        //noinspection ConstantConditions
        assertEquals("1", probability.toString());
    }

    @Test
    public void testEmptyProbability() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty probability. Use default value 100");
        getFromConfig(getPreparedConfig(
                "?: 100"),
                getDebugFearingCustomLogger(), "p", "probability");
    }

    @Test
    public void testEmptyProbabilityValue() throws Exception {
        assertEquals(null, getFromConfig(getPreparedConfig(
                "?: 100"),
                getParanoiacCustomLogger(), "p", "probability"));
    }

    @Test
    public void testNullProbability() throws Exception {
        assertEquals(null, getFromConfig(getPreparedConfig(
                "p: 100"),
                getParanoiacCustomLogger(), "p", "probability"));
    }

    @Test
    public void testPassNormalProbability() throws Exception {
        assertTrue(20 < getPasses(100, new Probability(50)));
    }

    @Test
    public void testPassSmallProbability() throws Exception {
        assertEquals(0, getPasses(10, new Probability(0)));
    }

    @Test
    public void testPassBigProbability() throws Exception {
        assertEquals(100, getPasses(10, new Probability(100)));
    }

    private int getPasses(int tries, Probability probability) {
        int passes = 0;
        for (int i = 0; i < tries; i++) {
            if (probability.pass()) {
                passes += 1;
            }
        }

        return Math.round(passes * 100 / tries);
    }

}