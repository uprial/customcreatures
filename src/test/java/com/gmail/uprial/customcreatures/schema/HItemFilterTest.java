package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemFilter.getFromConfig;
import static junit.framework.Assert.assertEquals;

public class HItemFilterTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeFilter() throws Exception {
        HItemFilter itemFilter = getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  reasons:",
                "    - NATURAL",
                "  probability: 50"),
                getParanoiacCustomLogger(), "f", "x");
        assertEquals("[types: [ZOMBIE], reasons: [NATURAL], probability: 50]", itemFilter.toString());
    }

    @Test
    public void testEmptyTypesFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty types of handler 'x'. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "f:",
                "  reasons:",
                "    - NATURAL",
                "  probability: 50"),
                getDebugFearingCustomLogger(), "f", "x");
    }

    @Test
    public void testEmptyReasonsFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty reasons of handler 'x'. Use default value NULL");
        getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  probability: 50"),
                getDebugFearingCustomLogger(), "f", "x");
    }

    @Test
    public void testEmptyProbabilityFilter() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty probability of handler 'x'. Use default value 100");
        getFromConfig(getPreparedConfig(
                "f:",
                "  types:",
                "    - ZOMBIE",
                "  reasons:",
                "    - NATURAL"),
                getDebugFearingCustomLogger(), "f", "x");
    }
}