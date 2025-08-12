package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

class CustomCreaturesCron extends BukkitRunnable {
    private static final int INTERVAL = 1;

    private static final Queue<Runnable> DEFERRED_TASKS = new LinkedBlockingQueue<>();
    private static final Queue<Runnable> ACTIVE_TASKS = new LinkedBlockingQueue<>();

    private final CustomLogger customLogger;
    private final int timeoutInMs;

    public CustomCreaturesCron(final CustomCreatures plugin,
                               final CustomLogger customLogger,
                               final int timeoutInMs) {
        this.customLogger = customLogger;
        this.timeoutInMs = timeoutInMs;

        runTaskTimer(plugin, INTERVAL, INTERVAL);
    }

    @Override
    public void cancel() {
        super.cancel();

        while(!DEFERRED_TASKS.isEmpty() || !ACTIVE_TASKS.isEmpty()) {
            run();
        }
    }

    public static void queue(Runnable task) {
        DEFERRED_TASKS.add(task);
    }

    @Override
    public void run() {
        final long start = System.currentTimeMillis();

        Runnable task;
        while((task = ACTIVE_TASKS.poll()) != null) {
            task.run();
        }
        while((task = DEFERRED_TASKS.poll()) != null) {
            ACTIVE_TASKS.add(task);
        }

        final long end = System.currentTimeMillis();
        if(end - start >= timeoutInMs) {
            customLogger.warning(String.format("Queue cron took %dms", end - start));
        }
    }
}