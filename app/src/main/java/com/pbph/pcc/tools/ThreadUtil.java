package com.pbph.pcc.tools;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {
    private static ThreadUtil instance = null;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);

    private ThreadUtil() {
    }

    public static ThreadUtil getInstance() {
        Class var0 = ThreadUtil.class;
        synchronized (ThreadUtil.class) {
            if (instance == null) {
                instance = new ThreadUtil();
            }
        }

        return instance;
    }

    public static void runMainThread(Runnable runnable) {
        (new Handler(Looper.getMainLooper())).post(runnable);
    }

    public void execute(Runnable runnable) {
        this.fixedThreadPool.execute(runnable);
    }

    public void shutdown() {
        this.fixedThreadPool.shutdown();
    }

    public void shutdownNow() {
        this.fixedThreadPool.shutdownNow();
    }
}

