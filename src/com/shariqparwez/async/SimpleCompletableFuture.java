package com.shariqparwez.async;

import java.util.concurrent.CompletableFuture;

public class SimpleCompletableFuture {
    public static void main(String[] args) {
        // ## 1 - Sample demonstration of completable future completion by using complete method as part of
        // Runnable implementation
        asyncOperationOne();
    }

    private static void asyncOperationOne() {
        // Build completable future using constructor
        CompletableFuture<Void> cf = new CompletableFuture<>();

        // Create runnable task, with use of complete method
        Runnable task = () -> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e){
            }
            cf.complete(null);
        };

        // Trigger the task
        CompletableFuture.runAsync(task);

        // Complete the task
        Void nil = cf.join();

        // Print some output
        System.out.println("We are done");
    }
}
