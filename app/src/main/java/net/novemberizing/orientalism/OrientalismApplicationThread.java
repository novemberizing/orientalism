package net.novemberizing.orientalism;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrientalismApplicationThread {
    private static ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void run(Runnable runnable) {
        pool.execute(runnable);
    }

    public static void main(Runnable runnable) {

    }
}
