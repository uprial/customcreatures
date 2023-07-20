package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.Probability.MAX_PERCENT;
import static com.gmail.uprial.customcreatures.schema.Probability.getFromConfig;
import static org.junit.Assert.*;

public class ProbabilityTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeProbability() throws Exception {
        Probability probability = getFromConfig(getPreparedConfig(
                "p: 1"),
                getParanoiacCustomLogger(), "p", "probability");
        assertNotNull(probability);
        assertEquals("1", probability.toString());
    }

    @Test
    public void testZeroProbability() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("A probability should be at least 0.0001");
        getFromConfig(getPreparedConfig(
                "p: 0"),
                getParanoiacCustomLogger(), "p", "probability");
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
        assertNull(getFromConfig(getPreparedConfig(
                "?: 100"),
                getCustomLogger(), "p", "probability"));
    }

    @Test
    public void testNullProbability() throws Exception {
        assertNull(getFromConfig(getPreparedConfig(
                "p: 100"),
                getParanoiacCustomLogger(), "p", "probability"));
    }

    @Test
    public void testPassNormalProbability() throws Exception {
        assertTrue(getPasses(100, new Probability(50)) > 20);
        assertTrue(getPasses(100, new Probability(50)) < 80);
    }

    @Test
    public void testPassSmallProbability() throws Exception {
        assertEquals(0, getPasses(10, new Probability(0)));
    }

    @Test
    public void testPassBigProbability() throws Exception {
        assertEquals(100, getPasses(10, new Probability(100)));
    }

    @Test
    public void testSmallProbability() throws Exception {
        assertEquals(0, getPasses(1000, new Probability(0.1)));
    }

    private static long getPasses(int tries, Probability probability) {
        int passes = 0;
        for (int i = 0; i < tries; i++) {
            if (probability.isPassed()) {
                passes += 1;
            }
        }

        return Math.round((double)passes / tries * MAX_PERCENT);
    }

}