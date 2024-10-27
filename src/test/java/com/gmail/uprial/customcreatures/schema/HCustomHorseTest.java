package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HCustomHorse.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HCustomHorseTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyHorse() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty horse");
        getFromConfig(getPreparedConfig(
                "?:"),
                getParanoiacCustomLogger(), "h", "horse");
    }

    @Test
    public void testNoModifications() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No modifications found of horse");

        getFromConfig(getPreparedConfig(
                "h:",
                " x: y"), getCustomLogger(), "h", "horse");
    }

    @Test
    public void testEmptyColor() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty color of horse. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "h: ",
                "  x: y"), getDebugFearingCustomLogger(), "h", "horse");
    }

    @Test
    public void testEmptyStyle() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty style of horse. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "h: ",
                "  color: WHITE"), getDebugFearingCustomLogger(), "h", "horse");
    }

    @Test
    public void testEmptyMaxDomestication() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty max. domestication of horse. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "h: ",
                "  color: WHITE",
                "  style: WHITEFIELD"), getDebugFearingCustomLogger(), "h", "horse");
    }

    @Test
    public void testEmptyJumpStrength() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty jump strength of horse. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "h: ",
                "  color: WHITE",
                "  style: WHITEFIELD",
                "  max-domestication: 100"), getDebugFearingCustomLogger(), "h", "horse");
    }


    @Test
    public void testWholeHorse() throws Exception {
        HCustomHorse customHorse = getFromConfig(getPreparedConfig(
                        "h: ",
                        "  color: WHITE",
                        "  style: WHITEFIELD",
                        "  max-domestication: 100",
                        "  jump-strength: 0.7"),
                getParanoiacCustomLogger(), "h", "horse");
        assertEquals("Horse{color: WHITE, style: WHITEFIELD, max-domestication: 100, jump-strength: 0.7}", customHorse.toString());
    }
}