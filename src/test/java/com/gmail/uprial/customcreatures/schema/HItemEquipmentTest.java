package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemEquipment.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HItemEquipmentTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeEquipment() throws Exception {
        HItemEquipment itemEquipment = getFromConfig(getPreparedConfig(
                "e:",
                " helmet:",
                "  material: IRON"),
                getParanoiacCustomLogger(), "e", "equipment");
        //noinspection ConstantConditions
        assertEquals("[helmet: [probability: null, material: IRON, enchantments: null, drop-chance: 0]]",
                itemEquipment.toString());
    }

    @Test
    public void testEmptyEquipment() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty equipment");
        getFromConfig(getPreparedConfig(
                "?:"),
                getDebugFearingCustomLogger(), "e", "equipment");
    }

    @Test
    public void testNoModifications() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No cloths found in equipment");
        getFromConfig(getPreparedConfig(
                "e:",
                " k: v"),
                getParanoiacCustomLogger(), "e", "equipment");
    }
}