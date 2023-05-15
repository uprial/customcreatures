package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Test;

import static com.gmail.uprial.customcreatures.schema.HItemDropExp.getFromConfig;
import static org.junit.Assert.*;

public class HItemDropExpTest extends TestConfigBase {
    @Test
    public void testWholeDropExp() throws Exception {
        HItemDropExp itemDropExp = getFromConfig(getPreparedConfig(
                "d: 1"),
                getParanoiacCustomLogger(), "d", "drop exp");
        assertNotNull(itemDropExp);
        assertEquals("1", itemDropExp.toString());
    }

    @Test
    public void testEmptyDropExp() throws Exception {
        HItemDropExp itemDropExp = getFromConfig(getPreparedConfig(
                "?: 1"),
                getCustomLogger(), "d", "drop exp");
        assertNull(itemDropExp);
    }
}