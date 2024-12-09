package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemSpawn.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HItemSpawnTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptySpawn() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty spawn. Use default value NULL");
        getFromConfig(getPreparedConfig(
                        "?:"),
                getParanoiacCustomLogger(), "s", "spawn");
    }

    @Test
    public void testNullType() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null type of spawn");
        getFromConfig(getPreparedConfig(
                        "s:",
                        " x: y"),
                getCustomLogger(), "s", "spawn");
    }

    @Test
    public void testEmptyAmount() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty amount of spawn. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "s: ",
                "  type: ILLUSIONER"), getDebugFearingCustomLogger(), "s", "spawn");
    }

    @Test
    public void testWholeSpawn() throws Exception {
        HItemSpawn spawn = getFromConfig(getPreparedConfig(
                        "s: ",
                        "  type: ILLUSIONER",
                        "  amount: 1"),
                getParanoiacCustomLogger(), "s", "spawn");
        assertEquals("{type: ILLUSIONER, amount: 1}", spawn.toString());
    }
}