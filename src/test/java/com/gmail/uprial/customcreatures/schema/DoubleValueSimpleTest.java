package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Test;

import static junit.framework.Assert.*;

public class DoubleValueSimpleTest extends TestConfigBase {
    @Test
    public void testDoubleValue() throws Exception {
        assertEquals(1, Math.round(DoubleValueSimple.getFromConfig(getPreparedConfig("v: 1.0"), "v").getValue()));
    }

}