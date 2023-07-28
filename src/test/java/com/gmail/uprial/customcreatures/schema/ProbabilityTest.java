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
        assertTrue(getPasses(1000, new Probability(50)) > 40);
        assertTrue(getPasses(1000, new Probability(50)) < 60);
    }

    @Test
    public void testPassZeroProbability() throws Exception {
        assertEquals(0, getPasses(1000, new Probability(0)));
    }

    @Test
    public void testPassSmallProbability() throws Exception {
        assertEquals(0, getPasses(1000, new Probability(0.1)));
    }

    @Test
    public void testPassBigProbability() throws Exception {
        assertEquals(100, getPasses(1000, new Probability(100)));
    }

    @Test
    public void testProbabilityDistribution() throws Exception {
        for(int i = 0; i <= 100; i++) {
            assertTrue(getPasses(1000, new Probability(i)) > i - 10);
            assertTrue(getPasses(1000, new Probability(i)) < i + 10);
        }
    }

    @Test
    public void testProbabilityDistribution_WithInc() throws Exception {
        final int INC = 11;
        for(int i = INC; i <= 100 - INC; i++) {
            final Probability probability = new Probability(i);
            int passes = 0;
            for (int j = 0; j < 1000; j++) {
                if (probability.isPassedWithInc(INC)) {
                    passes += 1;
                }
            }

            long lPasses = Math.round((double)passes / 1000 * MAX_PERCENT);

            assertTrue(lPasses > i - 10 + INC);
            assertTrue(lPasses < i + 10 + INC);
        }
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