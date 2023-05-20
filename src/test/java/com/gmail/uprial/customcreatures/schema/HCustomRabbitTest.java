package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HCustomRabbit.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HCustomRabbitTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyRabbit() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty rabbit");
        getFromConfig(getPreparedConfig(
                "?:"),
                getParanoiacCustomLogger(), "r", "rabbit");
    }

    @Test
    public void testNoModifications() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No modifications found of rabbit");

        getFromConfig(getPreparedConfig(
                "r:",
                " x: y"), getCustomLogger(), "r", "rabbit");
    }

    @Test
    public void testEmptyType() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty type of rabbit. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "r: ",
                "  x: y"), getDebugFearingCustomLogger(), "r", "rabbit");
    }

    @Test
    public void testWholeRabbit() throws Exception {
        HCustomRabbit customRabbit = getFromConfig(getPreparedConfig(
                        "r: ",
                        "  type: THE_KILLER_BUNNY"),
                getParanoiacCustomLogger(), "r", "rabbit");
        assertEquals("Rabbit{type: THE_KILLER_BUNNY}", customRabbit.toString());
    }
}