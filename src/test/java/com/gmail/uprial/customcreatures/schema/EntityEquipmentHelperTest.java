package com.gmail.uprial.customcreatures.schema;

import org.junit.Test;

import static com.gmail.uprial.customcreatures.common.DoubleHelper.MIN_DOUBLE_VALUE;
import static com.gmail.uprial.customcreatures.schema.EntityEquipmentHelper.isDropChanceNotEmpty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EntityEquipmentHelperTest {
    @Test
    public void testIsDropChanceNotEmpty() throws Exception {
        assertTrue(isDropChanceNotEmpty((float)MIN_DOUBLE_VALUE));
        assertTrue(isDropChanceNotEmpty((float)(MIN_DOUBLE_VALUE / 9.0D)));
        assertFalse(isDropChanceNotEmpty((float)(MIN_DOUBLE_VALUE / 11.0D)));
    }
}