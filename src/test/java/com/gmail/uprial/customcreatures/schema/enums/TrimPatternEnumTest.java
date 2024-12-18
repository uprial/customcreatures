package com.gmail.uprial.customcreatures.schema.enums;

import com.gmail.uprial.customcreatures.helpers.TestServerBase;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrimPatternEnumTest extends TestServerBase {
    @Test
    public void testConsistency() {
        assertEquals(TrimPatternEnum.values().length, TrimPattern.class.getFields().length);
    }
}