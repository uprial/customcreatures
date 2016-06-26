package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HValue.getDoubleFromConfig;
import static com.gmail.uprial.customcreatures.schema.HValue.getIntFromConfig;
import static org.junit.Assert.assertEquals;

public class HValueTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWrongDoubleType() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Wrong type of i of handler 'x'");

        getDoubleFromConfig(getPreparedConfig("i: 1z.0"), getParanoiacCustomLogger(), "i", "i of handler", "x");
    }

    @Test
    public void testWrongIntType() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Wrong type of i of handler 'x'");

        getIntFromConfig(getPreparedConfig("i: 1z.0"), getParanoiacCustomLogger(), "i", "i of handler", "x");
    }

    @Test
    public void testSimpleDoubleValue() throws Exception {
        //noinspection ConstantConditions
        assertEquals(42, Math.round(getDoubleFromConfig(getPreparedConfig("i: 42"),
                         getParanoiacCustomLogger(), "i", "i of handler", "x").getValue()));
    }

    @Test
    public void testSimpleIntValue() throws Exception {
        //noinspection ConstantConditions
        assertEquals(42, getIntFromConfig(getPreparedConfig("i: 42"),
                getParanoiacCustomLogger(), "i", "i of handler", "x").getValue());
    }

    @Test
    public void testRandomDoubleValue() throws Exception {
        //noinspection ConstantConditions
        assertEquals(2, Math.round(getDoubleFromConfig(getPreparedConfig(
                "i:",
                " type: random",
                " min: 2.0",
                " max: 2.0",
                " distribution: exp_up"),
                getParanoiacCustomLogger(), "i", "i of handler", "x").getValue()));
    }

    @Test
    public void testRandomIntValue() throws Exception {
        //noinspection ConstantConditions
        assertEquals(2, getIntFromConfig(getPreparedConfig(
                "i:",
                " type: random",
                " min: 2",
                " max: 2",
                " distribution: exp_up"),
                getParanoiacCustomLogger(), "i", "i of handler", "x").getValue());
    }

    @Test
    public void testEmptyDoubleValue() throws Exception {
        assertEquals(null, getDoubleFromConfig(getPreparedConfig(""),
                getCustomLogger(), "i", "i of handler", "x"));
    }

    @Test
    public void testEmptyIntValue() throws Exception {
        assertEquals(null, getIntFromConfig(getPreparedConfig(""),
                getCustomLogger(), "i", "i of handler", "x"));
    }

    @Test
    public void testEmptyDoubleValueMessage() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty i of handler 'x'. Use default value NULL");

        assertEquals(null, getDoubleFromConfig(getPreparedConfig(""),
                getDebugFearingCustomLogger(), "i", "i of handler", "x"));
    }

    @Test
    public void testEmptyIntValueMessage() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty i of handler 'x'. Use default value NULL");

        assertEquals(null, getIntFromConfig(getPreparedConfig(""),
                getDebugFearingCustomLogger(), "i", "i of handler", "x"));
    }

}