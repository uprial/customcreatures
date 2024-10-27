package com.gmail.uprial.customcreatures.schema.enums;

import com.gmail.uprial.customcreatures.helpers.TestServerBase;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrimMaterialEnumTest extends TestServerBase {
    @Test
    public void testConsistency() throws Exception {
        assertEquals(TrimMaterialEnum.values().length, TrimMaterial.class.getFields().length);
    }
}