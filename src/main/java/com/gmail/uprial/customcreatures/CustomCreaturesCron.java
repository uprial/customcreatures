package com.gmail.uprial.customcreatures;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

class CustomCreaturesCron extends BukkitRunnable {
    private static final int INTERVAL = 1;

    private final CustomCreatures plugin;

    private static final Queue<Runnable> DEFERRED_TASKS = new LinkedBlockingQueue<>();
    private static final Queue<Runnable> ACTIVE_TASKS = new LinkedBlockingQueue<>();

    public CustomCreaturesCron(CustomCreatures plugin) {
        this.plugin = plugin;
    }

    public BukkitTask runTaskTimer() {
        return runTaskTimer(plugin, INTERVAL, INTERVAL);
    }

    public static void defer(Runnable task) {
        DEFERRED_TASKS.add(task);
    }

    @Override
    public void run() {
        Runnable task;
        while((task = ACTIVE_TASKS.poll()) != null) {
            task.run();
        }
        while((task = DEFERRED_TASKS.poll()) != null) {
            ACTIVE_TASKS.add(task);
        }
    }
}