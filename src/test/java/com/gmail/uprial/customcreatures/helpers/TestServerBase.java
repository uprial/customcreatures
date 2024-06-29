package com.gmail.uprial.customcreatures.helpers;

import org.bukkit.*;
import org.junit.After;
import org.junit.Before;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

/*
    Code by @Jikoo as per
    https://www.spigotmc.org/threads/cant-create-enchantment-in-test.653255/#post-4747855
 */
public abstract class TestServerBase {
    @Before
    public void setUp() throws Exception {
        Server mock = mock(Server.class);
        Logger noOp = mock(Logger.class);

        when(mock.getLogger()).thenReturn(noOp);

        Bukkit.setServer(mock);

        // Bukkit has a lot of static constants referencing registry values. To initialize those, the
        // registries must be able to be fetched before the classes are touched.
        Map<Class<? extends Keyed>, Object> registers = new HashMap<>();

        doAnswer(invocationGetRegistry ->
                registers.computeIfAbsent(invocationGetRegistry.getArgument(0), clazz -> {
                    Registry<?> registry = mock(Registry.class);
                    Map<NamespacedKey, Keyed> cache = new HashMap<>();
                    doAnswer(invocationGetEntry -> {
                        NamespacedKey key = invocationGetEntry.getArgument(0);
                        // Some classes (like BlockType and ItemType) have extra generics that will be
                        // erased during runtime calls. To ensure accurate typing, grab the constant's field.
                        // This approach also allows us to return null for unsupported keys.
                        Class<? extends Keyed> constantClazz;
                        try {
                            //noinspection unchecked
                            constantClazz = (Class<? extends Keyed>) clazz.getField(key.getKey().toUpperCase(Locale.ROOT).replace('.', '_')).getType();
                        } catch (ClassCastException e) {
                            throw new RuntimeException(e);
                        } catch (NoSuchFieldException e) {
                            return null;
                        }

                        return cache.computeIfAbsent(key, key1 -> {
                            Keyed keyed = mock(constantClazz);
                            doReturn(key).when(keyed).getKey();
                            return keyed;
                        });
                    }).when(registry).get(notNull());

                    return registry;
                }))
                .when(mock).getRegistry(notNull());
    }

    @After
    public void tearDown() {
        try
        {
            Field server = Bukkit.class.getDeclaredField("server");
            server.setAccessible(true);
            server.set(null, null);
        }
        catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
