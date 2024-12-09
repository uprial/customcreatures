package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static com.gmail.uprial.customcreatures.schema.PlayerMultiplier.getFromConfig;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerMultiplierTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyPlayerMultiplier() throws Exception {
        e.expect(RuntimeException.class);
        e.expectMessage("Empty player-multiplier. Use default value NULL");
        getFromConfig(getPreparedConfig(
                        "?:"),
                getParanoiacCustomLogger(), "pm", "player-multiplier");
    }

    @Test
    public void testEmptySort() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null sort of player-multiplier");
        getFromConfig(getPreparedConfig(
                        "pm:",
                                " x: y"),
                getCustomLogger(), "pm", "player-multiplier");
    }

    @Test
    public void testEmptyStatistic() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null statistic of player-multiplier");
        getFromConfig(getPreparedConfig(
                        "pm:",
                        " sort: closest"),
                getCustomLogger(), "pm", "player-multiplier");
    }

    @Test
    public void testEmptyDivider() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null divider of player-multiplier");
        getFromConfig(getPreparedConfig(
                        "pm:",
                        " sort: closest",
                        " statistic: DAMAGE_DEALT"),
                getCustomLogger(), "pm", "player-multiplier");
    }

    @Test
    public void testEmptyMax() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null max of player-multiplier");
        getFromConfig(getPreparedConfig(
                        "pm:",
                        " sort: closest",
                        " statistic: DAMAGE_DEALT",
                        " divider: 5_000"),
                getCustomLogger(), "pm", "player-multiplier");
    }

    @Test
    public void testWholePlayerMultiplier() throws Exception {
        PlayerMultiplier psp = getFromConfig(getPreparedConfig(
                        "pm:",
                        " sort: closest",
                        " statistic: DAMAGE_DEALT",
                        " divider: 5_000",
                        " max: 5.0"),
                getCustomLogger(), "pm", "player-multiplier");
        assertEquals("{sort: CLOSEST, statistic: DAMAGE_DEALT, divider: 5000.0, max: 5.0}", psp.toString());
    }

    @Test
    public void testClosest() throws Exception {
        final Location location1 = mock(Location.class);
        when(location1.distance(any())).thenReturn(1.0D);

        final Player player1 = mock(Player.class);
        when(player1.getStatistic(Statistic.DAMAGE_DEALT)).thenReturn(1_000);
        when(player1.getLocation()).thenReturn(location1);

        final Location location2 = mock(Location.class);
        when(location2.distance(any())).thenReturn(2.0D);

        final Player player2 = mock(Player.class);
        when(player2.getStatistic(Statistic.DAMAGE_DEALT)).thenReturn(3_000);
        when(player2.getLocation()).thenReturn(location2);

        PlayerMultiplier psp = getFromConfig(getPreparedConfig(
                        "pm:",
                        " sort: closest",
                        " statistic: DAMAGE_DEALT",
                        " divider: 5_000",
                        " max: 5.0"),
                getCustomLogger(), "pm", "player-multiplier");
        assertEquals(1.2D, psp.get(mockLivingEntity(player1, player2)), 0.01D);
    }

    @Test
    public void testBiggest() throws Exception {
        final Player player1 = mock(Player.class);
        when(player1.getStatistic(Statistic.DAMAGE_DEALT)).thenReturn(1_000);

        final Player player2 = mock(Player.class);
        when(player2.getStatistic(Statistic.DAMAGE_DEALT)).thenReturn(3_000);

        PlayerMultiplier psp = getFromConfig(getPreparedConfig(
                        "pm:",
                        " sort: biggest",
                        " statistic: DAMAGE_DEALT",
                        " divider: 5_000",
                        " max: 5.0"),
                getCustomLogger(), "pm", "player-multiplier");
        assertEquals(1.6D, psp.get(mockLivingEntity(player1, player2)), 0.01D);
    }

    @Test
    public void testMax() throws Exception {
        final Player player1 = mock(Player.class);
        when(player1.getStatistic(Statistic.DAMAGE_DEALT)).thenReturn(1_000);

        final Player player2 = mock(Player.class);
        when(player2.getStatistic(Statistic.DAMAGE_DEALT)).thenReturn(3_000);

        PlayerMultiplier psp = getFromConfig(getPreparedConfig(
                        "pm:",
                        " sort: biggest",
                        " statistic: DAMAGE_DEALT",
                        " divider: 5_000",
                        " max: 1.4"),
                getCustomLogger(), "pm", "player-multiplier");

        assertEquals(1.2D, psp.get(mockLivingEntity(player1, player1)), 0.01D);
        assertEquals(1.4D, psp.get(mockLivingEntity(player2, player2)), 0.01D);
    }

    private LivingEntity mockLivingEntity(final Player player1, final Player player2) {
        final Collection<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        final World world = mock(World.class);
        when(world.getEntitiesByClass(Player.class)).thenReturn(players);

        final LivingEntity entity = mock(LivingEntity.class);
        when(entity.getWorld()).thenReturn(world);
        when(entity.getUniqueId()).thenReturn(UUID.randomUUID());

        return  entity;
    }
}