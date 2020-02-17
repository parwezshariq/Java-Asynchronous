package com.shariqparwez.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirstCompletableFutures {
    public static void main(String[] args) throws InterruptedException {
        // ## 1 - Sample demonstration of triggering asynchronous task within same thread
        //asyncOperationOne();

        // ## 2 - Running using executor service.
        //asyncOperationTwo();

        // ## 2 - Running the above snippet without using executor service this time.
        asyncOperationThree();

    }

    private static void asyncOperationOne() throws InterruptedException {
        // Create a completable future operation by providing runnable task as input.
        // Running this will not print any output
        CompletableFuture.runAsync(() -> System.out.println("I am running asynchronously!"));

        // This makes main thread to sleep, so that we can see output from above task running asynchronously
        Thread.sleep(100);
    }

    private static void asyncOperationTwo() throws InterruptedException {
        // Build executor, having single thread in pool
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create runnable task
        Runnable task = () -> System.out.println("I am running asynchronously in the thread " +
                Thread.currentThread().getName());

        // Build and trigger completable future operation by passing runnable task and executor
        CompletableFuture.runAsync(task, executor);

        // To end and close thread pool, once operation is complete
        executor.shutdown();
    }

    private static void asyncOperationThree() throws InterruptedException {
        // Create runnable task
        Runnable task = () -> System.out.println("I am running asynchronously in the thread " +
                Thread.currentThread().getName());

        // Build and trigger completable future operation by passing runnable task and executor
        CompletableFuture.runAsync(task);

        // Make the main thread sleep for 100ms
        Thread.sleep(100);
    }
}
