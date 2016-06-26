package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HValue.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HValueTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWrongType() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Wrong type of i of handler 'x'");

        getFromConfig(getPreparedConfig("i: 1z.0"), getParanoiacCustomLogger(), "i", "i of handler", "x");
    }

    @Test
    public void testSimpleValue() throws Exception {
        //noinspection ConstantConditions
        assertEquals(42, Math.round(getFromConfig(getPreparedConfig("i: 42"),
                         getParanoiacCustomLogger(), "i", "i of handler", "x").getValue()));
    }

    @Test
    public void testRandomValue() throws Exception {
        //noinspection ConstantConditions
        assertEquals(2, Math.round(getFromConfig(getPreparedConfig(
                "i:",
                " type: random",
                " min: 2.0",
                " max: 2.0",
                " distribution: exp_up"),
                getParanoiacCustomLogger(), "i", "i of handler", "x").getValue()));
    }

    @Test
    public void testEmptyValue() throws Exception {
        assertEquals(null, getFromConfig(getPreparedConfig(""),
                getCustomLogger(), "i", "i of handler", "x"));
    }

    @Test
    public void testEmptyValueMessage() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty i of handler 'x'. Use default value NULL");

        assertEquals(null, getFromConfig(getPreparedConfig(""),
                getDebugFearingCustomLogger(), "i", "i of handler", "x"));
    }
}