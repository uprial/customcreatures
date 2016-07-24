package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Test;

import static com.gmail.uprial.customcreatures.schema.HItemDurability.getFromConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class HItemDurabilityTest extends TestConfigBase {
    @Test
    public void testWholeDurability() throws Exception {
        HItemDurability itemDurability = getFromConfig(getPreparedConfig(
                "d: 1"),
                getParanoiacCustomLogger(), "d", "durability");
        assertNotNull(itemDurability);
        assertEquals("1", itemDurability.toString());
    }

    @Test
    public void testEmptyDurability() throws Exception {
        HItemDurability itemDurability = getFromConfig(getPreparedConfig(
                "?: 1"),
                getCustomLogger(), "d", "durability");
        assertNull(itemDurability);
    }
}