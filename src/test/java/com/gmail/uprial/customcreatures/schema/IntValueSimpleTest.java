package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class IntValueSimpleTest extends TestConfigBase {
    @Test
    public void testIntValue() throws Exception {
        assertEquals(1, (int)IntValueSimple.getFromConfig(getPreparedConfig("v: 1.6"), "v").getValue());
    }

}