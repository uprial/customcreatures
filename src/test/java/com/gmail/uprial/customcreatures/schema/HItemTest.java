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
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty filter of handler 'h'");

        getFromConfig(getPreparedConfig("h: "), getParanoiacCustomLogger(), "h");
    }

    @Test
    public void testEmptyEffects() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty effects of handler 'h'. Use default value NULL");

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
    public void testEmptyMaxHealth() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty max. health multiplier of handler 'h'. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "h: ",
                " filter:",
                "   types:",
                "     - ZOMBIE",
                "   reasons:",
                "     - NATURAL",
                "   probability: 100",
                " effects:",
                "  - e",
                " e:",
                "  types:",
                "   - SPEED",
                "  strength: 1",
                "  duration: 1"), getDebugFearingCustomLogger(), "h");
    }

    @Test
    public void testEmptyHandler() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No modifications found for handler 'h'");

        getFromConfig(getPreparedConfig(
                "h:",
                " filter:",
                "   probability: 99"), getCustomLogger(), "h");
    }

}