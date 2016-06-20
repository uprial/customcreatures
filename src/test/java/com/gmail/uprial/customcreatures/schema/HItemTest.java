package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItem.getFromConfig;

public class HItemTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty filter of handler 'h'. Use default value NULL");

        getFromConfig(getPreparedConfig("h: "), getDebugFearingCustomLogger(), "h");
    }

    @Test
    public void testConfigureMaxSpeed() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty speed multiplier of handler 'h'. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "h: ",
                " filter:",
                "   types:",
                "     - ZOMBIE",
                "   reasons:",
                "     - NATURAL",
                "   probability: 100"), getDebugFearingCustomLogger(), "h");
    }

    @Test
    public void testEmptyHandler() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No modifications found for handler 'h'");

        getFromConfig(getPreparedConfig("h:"), getCustomLogger(), "h");
    }

}