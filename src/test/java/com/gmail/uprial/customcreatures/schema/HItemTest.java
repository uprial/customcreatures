package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItem.getFromConfig;

public class HItemTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyFilter() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty filter of handler 'h'");

        getFromConfig(getPreparedConfig("h: "), getParanoiacCustomLogger(), "h");
    }

    @Test
    public void testEmptyEffects() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty effects of handler 'h'. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "h: ",
                " filter:",
                "   types:",
                "     - ZOMBIE",
                "   reasons:",
                "     - NATURAL",
                "   probability: 100"), getDebugFearingCustomLogger(), "h");
    }

    @Test
    public void testEmptyMaxHealth() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty max. health multiplier of handler 'h'. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "h: ",
                " filter:",
                "   types:",
                "     - ZOMBIE",
                "   reasons:",
                "     - NATURAL",
                "   probability: 100",
                " effects:",
                "  - e",
                " e:",
                "  types:",
                "   - SPEED",
                "  strength: 1",
                "  duration: 1"), getDebugFearingCustomLogger(), "h");
    }

    @Test
    public void testEmptyHandler() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No modifications found for handler 'h'");

        getFromConfig(getPreparedConfig(
                "h:",
                " filter:",
                "   probability: 99"), getCustomLogger(), "h");
    }

    @Test
    public void testEmptyEquipment() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty equipment of handler 'h'. Use default value NULL");

        getFromConfig(getPreparedConfig(
                "h: ",
                " filter:",
                "   types:",
                "     - ZOMBIE",
                "   reasons:",
                "     - NATURAL",
                "   probability: 100",
                " effects:",
                "  - e",
                " e:",
                "  types:",
                "   - SPEED",
                "  strength: 1",
                "  duration: 1",
                " max-health: 1.5"), getDebugFearingCustomLogger(), "h");
    }

    @Test
    public void testLongErrorMessageWithEffects() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid com.gmail.uprial.customcreatures.schema.RandomDistributionType" +
                " 'exp_dow' in distribution type of strength of effect 'h.acceleration' in effects of handler 'h'");
        getFromConfig(getPreparedConfig(
                "h:",
                " filter:",
                "  probability: 99",
                " effects:",
                "  - acceleration",
                " acceleration:",
                "  types:",
                "   - SPEED",
                "  strength:",
                "    type: random",
                "    min: 2",
                "    max: 10",
                "    distribution: exp_dow",
                "  duration: 99999"), getCustomLogger(), "h");
    }

    @Test
    public void testLongErrorMessageWithEquipment() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid com.gmail.uprial.customcreatures.schema.RandomDistributionType" +
                " 'exp_dow' in distribution type of level of enchantment 'h.equipment.helmet.e' in enchantments of helmet" +
                " of equipment of handler 'h'");
        getFromConfig(getPreparedConfig(
                "h:",
                " filter:",
                "  probability: 99",
                " equipment:",
                "  helmet:",
                "   material: IRON",
                "   enchantments:",
                "    - e",
                "   e:",
                "    type: THORNS",
                "    level:",
                "     type: random",
                "     min: 1",
                "     max: 4",
                "     distribution: exp_dow"), getCustomLogger(), "h");
    }

}