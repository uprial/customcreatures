package com.gmail.uprial.customcreatures;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

class CustomCreaturesCron extends BukkitRunnable {
    private static final int INTERVAL = 1;

    private static final Queue<Runnable> DEFERRED_TASKS = new LinkedBlockingQueue<>();
    private static final Queue<Runnable> ACTIVE_TASKS = new LinkedBlockingQueue<>();

    public CustomCreaturesCron(CustomCreatures plugin) {
        runTaskTimer(plugin, INTERVAL, INTERVAL);
    }

    @Override
    public void cancel() {
        super.cancel();

        while(!DEFERRED_TASKS.isEmpty() || !ACTIVE_TASKS.isEmpty()) {
            run();
        }
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