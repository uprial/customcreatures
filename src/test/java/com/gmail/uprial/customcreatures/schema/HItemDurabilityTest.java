package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Test;

import static com.gmail.uprial.customcreatures.schema.HItemDurability.getFromConfig;
import static org.junit.Assert.assertEquals;

public class HItemDurabilityTest extends TestConfigBase {
    @Test
    public void testWholeDurability() throws Exception {
        HItemDurability itemDurability = getFromConfig(getPreparedConfig(
                "d: 1"),
                getParanoiacCustomLogger(), "d", "durability");
        //noinspection ConstantConditions
        assertEquals("1", itemDurability.toString());
    }

    @Test
    public void testEmptyDurability() throws Exception {
        HItemDurability itemDurability = getFromConfig(getPreparedConfig(
                "?: 1"),
                getParanoiacCustomLogger(), "d", "durability");
        //noinspection ConstantConditions
        assertEquals(null, itemDurability);
    }
}