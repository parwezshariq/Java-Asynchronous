package com.shariqparwez.performant;

import com.shariqparwez.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TriggerExample {
    public static void main(String[] args) {
        // ## 1 -  Setting up of chained tasks
        //asyncPerformantOperationOne();

        // ## 2 -  Setting up the Delayed Start Pattern
        //asyncPerformantOperationTwo();

        // ## 2 -  Controlling Threads with the Delayed Start Pattern
        asyncPerformantOperationThree();
	}

    private static void asyncPerformantOperationOne() {
        // Create Supplier
        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        // Create Function
        Function<List<Long>, List<User>> fetchUsers = ids -> {
            sleep(300);
            return ids.stream().map(User::new).collect(Collectors.toList());
        };

        // Create Consumer
        Consumer<List<User>> displayer = users -> {
            System.out.println("In thread " + Thread.currentThread().getName());
            users.forEach(System.out::println);
        };

        // Chain all tasks together
        CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(supplyIDs);
        CompletableFuture<List<User>> fetch  = supply.thenApply(fetchUsers);
        CompletableFuture<Void> display = fetch.thenAccept(displayer);

        // Make main thread sleep so that tasks can get completed
        sleep(1_000);
    }

    private static void asyncPerformantOperationTwo() {
        // Create Supplier
        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        // Create Function
        Function<List<Long>, List<User>> fetchUsers = ids -> {
            sleep(300);
            return ids.stream().map(User::new).collect(Collectors.toList());
        };

        // Create Consumer
        Consumer<List<User>> displayer = users -> {
            System.out.println("In thread " + Thread.currentThread().getName());
            users.forEach(System.out::println);
        };

        // Build a completable future, using default constructor
        CompletableFuture<Void> start = new CompletableFuture<>();

        // Chain all tasks together
        // Use start.thenApply to get the supplyIDs
        CompletableFuture<List<Long>> supply = start.thenApply(nil -> supplyIDs.get());
        CompletableFuture<List<User>> fetch  = supply.thenApply(fetchUsers);
        CompletableFuture<Void> display = fetch.thenAccept(displayer);

        // Complete the completable future 'start' task
        start.complete(null);

        // Make main thread sleep so that tasks can get completed
        sleep(1_000);
    }

    private static void asyncPerformantOperationThree() {
        // Create Executor Service thread pool with single thread
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create Supplier
        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        // Create Function
        Function<List<Long>, List<User>> fetchUsers = ids -> {
            sleep(300);
            return ids.stream().map(User::new).collect(Collectors.toList());
        };

        // Create Consumer
        Consumer<List<User>> displayer = users -> {
            System.out.println("In thread " + Thread.currentThread().getName());
            users.forEach(System.out::println);
        };

        // Build a completable future, using default constructor
        CompletableFuture<Void> start = new CompletableFuture<>();

        // Chain all tasks together
        // Use start.thenApply to get the supplyIDs
        CompletableFuture<List<Long>> supply = start.thenApply(nil -> supplyIDs.get());
        CompletableFuture<List<User>> fetch  = supply.thenApply(fetchUsers);
        CompletableFuture<Void> display = fetch.thenAccept(displayer);

        // Complete the completable future 'start' task
        // Running in thread pool
        start.completeAsync(() -> null, executor);

        // Make main thread sleep so that tasks can get completed
        sleep(1_000);

        // Shutdown executor to let JVM terminate
        executor.shutdown();
    }

    private static void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}
}
