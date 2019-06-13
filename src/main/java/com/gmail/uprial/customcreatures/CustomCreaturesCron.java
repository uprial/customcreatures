package com.gmail.uprial.customcreatures;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

class CustomCreaturesCron implements Runnable {
    private static final int INTERVAL = 1;

    private static final Queue<Runnable> DEFERRED_TASKS = new LinkedBlockingQueue<>();
    private static final Queue<Runnable> ACTIVE_TASKS = new LinkedBlockingQueue<>();

    public static void defer(Runnable task) {
        DEFERRED_TASKS.add(task);
    }

    public static int getInterval() {
        return INTERVAL;
    }

    @Override
    public void run() {
        Runnable task;
        //noinspection NestedAssignment,MethodCallInLoopCondition
        while((task = ACTIVE_TASKS.poll()) != null) {
            task.run();
        }
        //noinspection NestedAssignment,MethodCallInLoopCondition
        while((task = DEFERRED_TASKS.poll()) != null) {
            ACTIVE_TASKS.add(task);
        }
    }
}