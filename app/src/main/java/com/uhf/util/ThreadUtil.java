package com.uhf.util;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Author CYD
 * Date 2019/1/4
 *
 */
public class ThreadUtil {
    private ExecutorService executors;

    private ThreadUtil() {
        executors = Executors.newFixedThreadPool(3);
    }

    public static ThreadUtil getInstance() {
        return MySingleton.instance;
    }

    static class MySingleton {
        static final ThreadUtil instance = new ThreadUtil();
    }

    public ExecutorService getExService() {
        return executors;
    }



}
