package com.gmail.uprial.customcreatures.schema;

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
    public void testEmptyEffectsValue() throws Exception {
        HItemEffectsList itemEffectsList = getFromConfig(getPreparedConfig(
                "ee:"),
                getCustomLogger(), "ee", "effects list");
        assertEquals(null, itemEffectsList);
    }
}