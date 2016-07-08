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
                getParanoiacCustomLogger(), "ee", "effects list");
        //noinspection ConstantConditions
        assertEquals("{[types: [SPEED], strength: 1, duration: 1]}", itemEffectsList.toString());
    }

    @Test
    public void testNoEffects() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty effects list. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "?:"),
                getDebugFearingCustomLogger(), "ee", "effects list");
    }

    @Test
    public void testEmptyEffectsList() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty effects list. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "ee:"),
                getDebugFearingCustomLogger(), "ee", "effects list");
    }

    @Test
    public void testEmptyEffectsValue() throws Exception {
        HItemEffectsList itemEffectsList = getFromConfig(getPreparedConfig(
                "ee:"),
                getParanoiacCustomLogger(), "ee", "effects list");
        assertEquals(null, itemEffectsList);
    }

    @Test
    public void testNoDefinitionOfKeyInEffects() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null definition of key 'e' in effects list");
        getFromConfig(getPreparedConfig(
                "ee:",
                " - e"),
                getParanoiacCustomLogger(), "ee", "effects list");
    }

    @Test
    public void testNoDefinitionOfKeyWithParentsInEffects() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null definition of keys 'x.e' and 'e' in effects list");
        getFromConfig(getPreparedConfig(
                "x:",
                " ee:",
                "  - e"),
                getParanoiacCustomLogger(), "x.ee", "effects list");
    }

    @Test
    public void testNotUniqueKeyInEffects() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Key 'e' in effects list is not unique");
        getFromConfig(getPreparedConfig(
                "ee:",
                " - e",
                " - e",
                "e:",
                " types:",
                "  - SPEED",
                " strength: 1",
                " duration: 1"),
                getParanoiacCustomLogger(), "ee", "effects list");
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
                getParanoiacCustomLogger(), "x.ee", "effects list");
        //noinspection ConstantConditions
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
                getParanoiacCustomLogger(), "x.ee", "effects list");
        //noinspection ConstantConditions
        assertEquals("{[types: [SPEED], strength: 1, duration: 1]}", itemEffectsList.toString());
    }

}