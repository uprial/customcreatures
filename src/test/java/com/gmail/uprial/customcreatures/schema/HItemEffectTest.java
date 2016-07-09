package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemEffect.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HItemEffectTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeEffect() throws Exception {
        HItemEffect itemEffect = getFromConfig(getPreparedConfig(
                "e:",
                "  types:",
                "    - SPEED",
                "  strength: 1",
                "  duration: 1"),
                getParanoiacCustomLogger(), "e", "effect");
        //noinspection ConstantConditions
        assertEquals("[types: [SPEED], strength: 1, duration: 1]", itemEffect.toString());
    }

    @Test
    public void testEmptyEffect() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty effect");
        getFromConfig(getPreparedConfig(
                "?:"),
                getParanoiacCustomLogger(), "e", "effect");
    }

    @Test
    public void testEmptyTypes() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty effect types of effect");
        getFromConfig(getPreparedConfig(
                "e:",
                "  types:"),
                getParanoiacCustomLogger(), "e", "effect");
    }

    @Test
    public void testEmptyStrength() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty strength of effect");
        getFromConfig(getPreparedConfig(
                "e:",
                "  types:",
                "    - SPEED"),
                getParanoiacCustomLogger(), "e", "effect");
    }

    @Test
    public void testEmptyDuration() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty duration of effect");
        getFromConfig(getPreparedConfig(
                "e:",
                "  types:",
                "    - SPEED",
                "  strength: 1"),
                getParanoiacCustomLogger(), "e", "effect");
    }

}