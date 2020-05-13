package com.example.popularmovies.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Project Popular Movies
 * @Created_by Mayank Kumar on 13-05-2020 02:15 PM
 */
public class DBExecutor {
    //Singleton instantiation
    private static final Object LOCK = new Object();
    private static DBExecutor sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private DBExecutor(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static DBExecutor getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new DBExecutor(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
