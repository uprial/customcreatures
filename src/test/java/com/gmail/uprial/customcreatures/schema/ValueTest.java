package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.Value.getFromConfig;
import static org.junit.Assert.assertEquals;

public class ValueTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWrongType() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Wrong type of i of handler 'x'");

        getFromConfig(getPreparedConfig("i: 1.0"), getParanoiacCustomLogger(), "i", "i of handler", "x");
    }

    @Test
    public void testSimpleValue() throws Exception {
        assertEquals(42, getFromConfig(getPreparedConfig("i: 42"),
                         getParanoiacCustomLogger(), "i", "i of handler", "x").getValue());
    }

    @Test
    public void testRandomValue() throws Exception {
        assertEquals(2, getFromConfig(getPreparedConfig(
                "i:",
                " type: random",
                " min: 2",
                " max: 2",
                " distribution: exp_up"),
                getParanoiacCustomLogger(), "i", "i of handler", "x").getValue());
    }
}