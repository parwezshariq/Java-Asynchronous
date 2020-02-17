package com.shariqparwez.asyncchain;

import com.shariqparwez.asyncchain.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AsyncExample {
    public static void main(String[] args) {
        // ## 1 -  Sample demonstration of chaining different tasks
        //asyncChainOperationOne();

        // ## 2 -  Sample demonstration of chaining different task and plugging one task in special thread
        //asyncChainOperationTwo();

        // ## 3 - Sample demonstration of chaining different task in Asynchronous way
        //asyncChainOperationThree();

        // ## 4 - Sample demonstration of chaining different task in Asynchronous way
        // Checking which thread sync composition is executed
        asyncChainOperationFour();
    }

    private static void asyncChainOperationOne() {
        // Create Supplier to supply list of IDs
        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        // Create Function to return list of user from list of IDs
        Function<List<Long>, List<User>> fetchUsers = ids -> {
            sleep(300);
            return ids.stream().map(User::new).collect(Collectors.toList());
        };

        // Create Consumer to display list of user
        Consumer<List<User>> displayer = users -> users.forEach(System.out::println);

        // Create completable future for supplyIds (supplier) task
        CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);

        // Chain fetchUsers (function) and displayer (consumer) task to completable future
        completableFuture.thenApply(fetchUsers)
                .thenAccept(displayer);

        // Giving the time for asynchronous activities to complete by making main thread to sleep
        sleep(1_000);
    }

    private static void asyncChainOperationTwo() {
        // Create Executor for pool of single thread
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create Supplier to supply list of IDs
        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        // Create Function to return list of user from list of IDs
        Function<List<Long>, List<User>> fetchUsers = ids -> {
            sleep(300);
            return ids.stream().map(User::new).collect(Collectors.toList());
        };

        // Create Consumer to display list of user
        Consumer<List<User>> displayer = users -> {
            System.out.println("Running in " + Thread.currentThread().getName());
            users.forEach(System.out::println);
        };

        // Create completable future for supplyIds (supplier) task
        CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);

        // Chain fetchUsers (function) and displayer (consumer) task to completable future
        // Displayer task to be executed through executor
        completableFuture.thenApply(fetchUsers)
                .thenAcceptAsync(displayer, executor);

        // Giving the time for asynchronous activities to complete by making main thread to sleep
        sleep(1_000);

        // Close executor so that JVM can terminate
        executor.shutdown();
    }

    private static void asyncChainOperationThree() {
        // Create Executor for pool of single thread
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create Supplier to supply list of IDs
        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        // Create Function to return list of user (as CompletableFuture) from list of IDs
        Function<List<Long>, CompletableFuture<List<User>>> fetchUsers = ids -> {
            sleep(300);
            Supplier<List<User>> userSupplier =
                    () -> {
                        System.out.println("Currently running in " + Thread.currentThread().getName());
                        return ids.stream().map(User::new).collect(Collectors.toList());
                    };
            return CompletableFuture.supplyAsync(userSupplier);
        };

        // Create Consumer to display list of user
        Consumer<List<User>> displayer = users -> {
            System.out.println("Running in " + Thread.currentThread().getName());
            users.forEach(System.out::println);
        };

        // Create completable future for supplyIds (supplier) task
        CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);

        // Chain fetchUsers (function) and displayer (consumer) task to completable future
        // Displayer task to be executed through executor
        // Usage of thenCompose to run asynchronously
        completableFuture.thenCompose(fetchUsers)
                .thenAcceptAsync(displayer, executor);

        // Giving the time for asynchronous activities to complete by making main thread to sleep
        sleep(1_000);

        // Close executor so that JVM can terminate
        executor.shutdown();
    }

    private static void asyncChainOperationFour() {
        // Create separate Executors for pool of single thread
        ExecutorService executorOne = Executors.newSingleThreadExecutor();
        ExecutorService executorTwo = Executors.newSingleThreadExecutor();

        // Create Supplier to supply list of IDs
        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        // Create Function to return list of user (as CompletableFuture) from list of IDs
        // userSupplier task to be executed through executor
        Function<List<Long>, CompletableFuture<List<User>>> fetchUsers = ids -> {
            sleep(300);
            System.out.println("Function is currently running in " + Thread.currentThread().getName());
            Supplier<List<User>> userSupplier =
                    () -> {
                        System.out.println("Currently running in " + Thread.currentThread().getName());
                        return ids.stream().map(User::new).collect(Collectors.toList());
                    };
            return CompletableFuture.supplyAsync(userSupplier, executorTwo);
        };

        // Create Consumer to display list of user
        Consumer<List<User>> displayer = users -> {
            System.out.println("Running in " + Thread.currentThread().getName());
            users.forEach(System.out::println);
        };

        // Create completable future for supplyIds (supplier) task
        CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);

        // Chain fetchUsers (function) and displayer (consumer) task to completable future
        // Displayer task to be executed through executor
        // Usage of thenCompose to run asynchronously
        // Function task to be executed through executor
        completableFuture.thenComposeAsync(fetchUsers, executorTwo)
                .thenAcceptAsync(displayer, executorOne);

        // Giving the time for asynchronous activities to complete by making main thread to sleep
        sleep(1_000);

        // Close executor so that JVM can terminate
        executorOne.shutdown();
        executorTwo.shutdown();
    }

    private static void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
        }
    }
}
