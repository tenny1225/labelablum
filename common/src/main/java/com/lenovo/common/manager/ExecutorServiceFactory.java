package com.lenovo.common.manager;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by noahkong on 17-6-5.
 */

public class ExecutorServiceFactory {
    private static ExecutorService searchExecutor;
    public static ExecutorService createSearchExecutor(){
        if(searchExecutor==null){
            searchExecutor = Executors.newFixedThreadPool(4);
        }
        return searchExecutor;
    }

    private static ExecutorService algorithmExecutor;
    public static ExecutorService  createAlgorithmExecutor(){
        if(algorithmExecutor==null){
            algorithmExecutor = Executors.newFixedThreadPool(4);
        }
        return algorithmExecutor;
    }
}
