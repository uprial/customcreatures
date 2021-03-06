package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import com.gmail.uprial.customcreatures.schema.numerics.IValue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HValue.getDoubleFromConfig;
import static com.gmail.uprial.customcreatures.schema.HValue.getIntFromConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class HValueTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWrongDoubleType() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Wrong type of i");

        getDoubleFromConfig(getPreparedConfig("i: 1z.0"), getParanoiacCustomLogger(), "i", "i", 0, 100);
    }

    @Test
    public void testWrongIntType() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Wrong type of i");

        getIntFromConfig(getPreparedConfig("i: 1z.0"), getParanoiacCustomLogger(), "i", "i", 0, 100);
    }

    @Test
    public void testSimpleDoubleValue() throws Exception {
        IValue<Double> value = getDoubleFromConfig(getPreparedConfig("i: 42"),
                getParanoiacCustomLogger(), "i", "i", 0, 100);
        assertNotNull(value);
        assertEquals(42, Math.round(value.getValue()));
    }

    @Test
    public void testSimpleIntValue() throws Exception {
        IValue<Integer> value = getIntFromConfig(getPreparedConfig("i: 42"),
                getParanoiacCustomLogger(), "i", "i", 0, 100);
        assertNotNull(value);
        assertEquals(42, value.getValue().intValue());
    }

    @Test
    public void testRandomDoubleValue() throws Exception {
        IValue<Double> value = getDoubleFromConfig(getPreparedConfig(
                "i:",
                " type: random",
                " min: 2.0",
                " max: 2.0",
                " distribution: exp_up"),
                getParanoiacCustomLogger(), "i", "i", 0, 100);
        assertNotNull(value);
        assertEquals(2, Math.round(value.getValue()));
    }

    @Test
    public void testRandomIntValue() throws Exception {
        IValue<Integer> value = getIntFromConfig(getPreparedConfig(
                "i:",
                " type: random",
                " min: 2",
                " max: 2",
                " distribution: exp_up"),
                getParanoiacCustomLogger(), "i", "i", 0, 100);
        assertNotNull(value);
        assertEquals(2, value.getValue().intValue());
    }

    @Test
    public void testEmptyDoubleValue() throws Exception {
        assertNull(getDoubleFromConfig(getPreparedConfig(""),
                getCustomLogger(), "i", "i", 0, 100));
    }

    @Test
    public void testEmptyIntValue() throws Exception {
        assertNull(getIntFromConfig(getPreparedConfig(""),
                getCustomLogger(), "i", "i", 0, 100));
    }

    @Test
    public void testEmptyDoubleValueMessage() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty i. Use default value NULL");

        assertNull(getDoubleFromConfig(getPreparedConfig(""),
                getDebugFearingCustomLogger(), "i", "i", 0, 100));
    }

    @Test
    public void testEmptyIntValueMessage() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty i. Use default value NULL");

        assertNull(getIntFromConfig(getPreparedConfig(""),
                getDebugFearingCustomLogger(), "i", "i", 0, 100));
    }
}