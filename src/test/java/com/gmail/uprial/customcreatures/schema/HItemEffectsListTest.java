package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemEffectsList.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HItemEffectsListTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeEffectsList() throws Exception {
        HItemEffectsList itemEffectsList = getFromConfig(getPreparedConfig(
                "ee:",
                "  - e",
                "e:",
                "  types:",
                "    - SPEED",
                "  strength: 1",
                "  duration: 1"),
                getParanoiacCustomLogger(), "ee", "effects list of handler", "x");
        //noinspection ConstantConditions
        assertEquals("{[types: [SPEED], strength: 1, duration: 1]}", itemEffectsList.toString());
    }

    @Test
    public void testNoEffects() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty effects list of handler 'x'. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "?:"),
                getDebugFearingCustomLogger(), "ee", "effects list of handler", "x");
    }

    @Test
    public void testEmptyEffectsList() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty effects list of handler 'x'. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "ee:"),
                getDebugFearingCustomLogger(), "ee", "effects list of handler", "x");
    }

    @Test
    public void testEmptyEffectsValue() throws Exception {
        HItemEffectsList itemEffectsList = getFromConfig(getPreparedConfig(
                "ee:"),
                getParanoiacCustomLogger(), "ee", "effects list of handler", "x");
        assertEquals(null, itemEffectsList);
    }

    @Test
    public void testNullKeyInEffects() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null key in effects list of handler 'x' at pos 0");
        getFromConfig(getPreparedConfig(
                "ee:",
                " -"),
                getParanoiacCustomLogger(), "ee", "effects list of handler", "x");
    }

    @Test
    public void testNotStringKeyInEffects() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Key '{e=null}' in effects list of handler 'x' at pos 0 is not a string");
        getFromConfig(getPreparedConfig(
                "ee:",
                " - e:"),
                getParanoiacCustomLogger(), "ee", "effects list of handler", "x");
    }

    @Test
    public void testEmptyKeyInEffects() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty key in effects list of handler 'x' at pos 0");
        getFromConfig(getPreparedConfig(
                "ee:",
                " - ''"),
                getParanoiacCustomLogger(), "ee", "effects list of handler", "x");
    }

    @Test
    public void testNoDefinitionOfKeyInEffects() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null definition of key 'e' in effects list of handler 'x'");
        getFromConfig(getPreparedConfig(
                "ee:",
                " - e"),
                getParanoiacCustomLogger(), "ee", "effects list of handler", "x");
    }

    @Test
    public void testNoDefinitionOfKeyWithParetnsInEffects() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null definition of keys 'x.e' and 'e' in effects list of handler 'x'");
        getFromConfig(getPreparedConfig(
                "x:",
                " ee:",
                "  - e"),
                getParanoiacCustomLogger(), "x.ee", "effects list of handler", "x");
    }

    @Test
    public void testNotUniqueKeyInEffects() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Key 'e' in effects list of handler 'x' is not unique");
        getFromConfig(getPreparedConfig(
                "ee:",
                " - e",
                " - e",
                "e:",
                " types:",
                "  - SPEED",
                " strength: 1",
                " duration: 1"),
                getParanoiacCustomLogger(), "ee", "effects list of handler", "x");
    }

    @Test
    public void testAbsoluteEffectPath() throws Exception {
        HItemEffectsList itemEffectsList = getFromConfig(getPreparedConfig(
                "x:",
                " ee:",
                "  - x.e",
                " e:",
                "  types:",
                "   - SPEED",
                "  strength: 1",
                "  duration: 1"),
                getParanoiacCustomLogger(), "x.ee", "effects list of handler", "x");
        assertEquals("{[types: [SPEED], strength: 1, duration: 1]}", itemEffectsList.toString());
    }

    @Test
    public void testRelativeEffectPath() throws Exception {
        HItemEffectsList itemEffectsList = getFromConfig(getPreparedConfig(
                "x:",
                " ee:",
                "  - e",
                " e:",
                "  types:",
                "   - SPEED",
                "  strength: 1",
                "  duration: 1"),
                getParanoiacCustomLogger(), "x.ee", "effects list of handler", "x");
        assertEquals("{[types: [SPEED], strength: 1, duration: 1]}", itemEffectsList.toString());
    }

}