package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.EntityType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.HItemEntitySpecificAttributes.getFromConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HItemEntitySpecificAttributesTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyESA() throws Exception {
        assertNull(getFromConfig(getPreparedConfig(
                "?:"),
                getParanoiacCustomLogger(), getItemFilter(EntityType.ZOMBIE), "esa", "e-s-a"));
    }

    @Test
    public void testNoModifications() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No modifications found of e-s-a");

        getFromConfig(getPreparedConfig(
                "esa:",
                " x: y"), getCustomLogger(), getItemFilter(EntityType.ZOMBIE),"esa", "e-s-a");
    }

    @Test
    public void testWholeCreeper() throws Exception {
        HItemEntitySpecificAttributes esa = getFromConfig(getPreparedConfig(
                        "esa: ",
                        "  creeper: ",
                        "    max-fuse-ticks: 10",
                        "    explosion-radius: 1",
                        "    powered: true"),
                getParanoiacCustomLogger(), getItemFilter(EntityType.CREEPER),"esa", "e-s-a");
        assertEquals("Creeper{max-fuse-ticks: 10, explosion-radius: 1, powered: true}", esa.toString());
    }

    @Test
    public void testWholeHorse() throws Exception {
        HItemEntitySpecificAttributes esa = getFromConfig(getPreparedConfig(
                        "esa: ",
                        "  horse: ",
                        "    color: WHITE",
                        "    style: WHITEFIELD",
                        "    max-domestication: 100",
                        "    jump-strength: 0.7",
                        "    tamed: false"),
                getParanoiacCustomLogger(), getItemFilter(EntityType.HORSE),"esa", "e-s-a");
        assertEquals("Horse{color: WHITE, style: WHITEFIELD, max-domestication: 100, jump-strength: 0.7, tamed: false}", esa.toString());
    }

    @Test
    public void testWholeZombieHorse() throws Exception {
        HItemEntitySpecificAttributes esa = getFromConfig(getPreparedConfig(
                        "esa: ",
                        "  zombie-horse: ",
                        "    jump-strength: 0.7",
                        "    tamed: false"),
                getParanoiacCustomLogger(), getItemFilter(EntityType.ZOMBIE_HORSE),"esa", "e-s-a");
        assertEquals("Horse{jump-strength: 0.7, tamed: false}", esa.toString());
    }

    @Test
    public void testWholeRabbit() throws Exception {
        HItemEntitySpecificAttributes esa = getFromConfig(getPreparedConfig(
                        "esa: ",
                        "  rabbit: ",
                        "    type: THE_KILLER_BUNNY"),
                getParanoiacCustomLogger(), getItemFilter(EntityType.RABBIT),"esa", "e-s-a");
        assertEquals("Rabbit{type: THE_KILLER_BUNNY}", esa.toString());
    }

    @Test
    public void testWrongFilter() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Possible entity types of filter of e-s-a ([RABBIT])" +
                " are wider than of horse of e-s-a ([HORSE]): [RABBIT]");

        HItemFilter itemFilter = HItemFilter.getFromConfig(getPreparedConfig(
                        "f:",
                        "  types:",
                        "    - RABBIT"),
                getCustomLogger(), "f", "filter");
        getFromConfig(getPreparedConfig(
                        "esa: ",
                        "  horse: ",
                        "    color: WHITE"),
                getCustomLogger(), itemFilter,"esa", "e-s-a");
    }

    private HItemFilter getItemFilter(final EntityType entityType) throws InvalidConfigurationException, InvalidConfigException {
        return HItemFilter.getFromConfig(getPreparedConfig(
                        "f:",
                        "  types:",
                        "    - " + entityType.toString()),
                getCustomLogger(), "f", "filter");
    }
}