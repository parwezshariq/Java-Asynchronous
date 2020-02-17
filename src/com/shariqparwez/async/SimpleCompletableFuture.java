package com.shariqparwez.async;

import java.util.concurrent.CompletableFuture;

public class SimpleCompletableFuture {
    public static void main(String[] args) {
        // ## 1 - Sample demonstration of completable future completion by using complete method as part of
        // Runnable implementation
        asyncOperationOne();
    }

    private static void asyncOperationOne() {
        CompletableFuture<Void> cf = new CompletableFuture<>();

        Runnable task = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e){
            }
            cf.complete(null);
        };

        CompletableFuture.runAsync(task);

        Void nil = cf.join();
        System.out.println("We are done");
    }
}
