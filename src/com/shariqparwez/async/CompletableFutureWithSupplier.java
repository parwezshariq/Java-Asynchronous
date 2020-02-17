package com.shariqparwez.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class CompletableFutureWithSupplier {
    public static void main(String[] args) {
        // ## 1 -  Sample demonstration of completable future using supplier
        //asyncOperationOne();

        // ## 2 -  Sample demonstration of completable future using supplier (with thread sleep)
        //asyncOperationTwo();

        // ## 3 -  Sample demonstration of completable future through executor service using supplier (with thread sleep)
        //asyncOperationThree();

        // ## 4 -  Sample demonstration of forcing result of completable future through executor service using supplier (with thread sleep)
        //asyncOperationFour();

        // ## 5 -  Sample demonstration of forcing result of completable future (by giving enough time to complete)
        // through executor service using supplier (with thread sleep)
        //asyncOperationFive();

        // ## 6 -  Sample demonstration of obtruding result of completable future (by giving enough time to complete)
        // through executor service using supplier (with thread sleep)
        //asyncOperationSix();

        // ## 6 -  Sample demonstration of obtruding result of completable future
        // through executor service using supplier (with thread sleep)
        asyncOperationSeven();
    }

    private static void asyncOperationOne() {
        // Create supplier
        Supplier<String> supplier = () -> Thread.currentThread().getName();

        // Build completable future using the supplier
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier);

        // Fetch result from completable future
        String result = completableFuture.join();

        // Print result
        System.out.println("Result = " + result);
    }

    private static void asyncOperationTwo() {
        // Create supplier with thread sleep
        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return Thread.currentThread().getName();
        };

        // Build completable future using the supplier
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier);

        // Fetch result from completable future
        String result = completableFuture.join();

        // Print result
        System.out.println("Result = " + result);
    }

    private static void asyncOperationThree() {
        // Build executor, having single thread in pool
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create supplier with thread sleep
        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return Thread.currentThread().getName();
        };

        // Build completable future using the supplier
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executor);

        // Fetch result from completable future
        String result = completableFuture.join();

        // Print result
        System.out.println("Result = " + result);

        // To end and close thread pool, once operation is complete
        executor.shutdown();
    }

    private static void asyncOperationFour() {
        // Build executor, having single thread in pool
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create supplier with thread sleep
        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return Thread.currentThread().getName();
        };

        // Build completable future using the supplier
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executor);

        // Force value for result of completable future if execution has not been complete yet
        completableFuture.complete("Too long!");

        // Fetch result from completable future
        String result = completableFuture.join();

        // Print result
        System.out.println("Result = " + result);

        // To end and close thread pool, once operation is complete
        executor.shutdown();
    }

    private static void asyncOperationFive() {
        // Build executor, having single thread in pool
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create supplier with thread sleep
        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return Thread.currentThread().getName();
        };

        // Build completable future using the supplier
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executor);

        // Fetch result from completable future
        String result = completableFuture.join();

        // Print result
        System.out.println("Result = " + result);

        // Force value for result of completable future if execution has not been complete yet
        completableFuture.complete("Too long!");

        // Fetch result from completable future
        result = completableFuture.join();

        // Print result
        System.out.println("Result = " + result);

        // To end and close thread pool, once operation is complete
        executor.shutdown();
    }

    private static void asyncOperationSix() {
        // Build executor, having single thread in pool
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create supplier with thread sleep
        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return Thread.currentThread().getName();
        };

        // Build completable future using the supplier
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executor);

        // Fetch result from completable future
        String result = completableFuture.join();

        // Print result
        System.out.println("Result = " + result);

        // Obtrude (over-write) value for result of completable future if execution has not been complete yet
        completableFuture.obtrudeValue("Too long!");

        // Fetch result from completable future
        result = completableFuture.join();

        // Print result
        System.out.println("Result = " + result);

        // To end and close thread pool, once operation is complete
        executor.shutdown();
    }

    private static void asyncOperationSeven() {
        // Build executor, having single thread in pool
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create supplier with thread sleep
        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return Thread.currentThread().getName();
        };

        // Build completable future using the supplier
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executor);

        // Obtrude (over-write) value for result of completable future if execution has not been complete yet
        completableFuture.obtrudeValue("Too long!");

        // Fetch result from completable future
        String result = completableFuture.join();

        // Print result
        System.out.println("Result = " + result);

        // Fetch result from completable future
        result = completableFuture.join();

        // Print result
        System.out.println("Result = " + result);

        // To end and close thread pool, once operation is complete
        executor.shutdown();
    }
}
