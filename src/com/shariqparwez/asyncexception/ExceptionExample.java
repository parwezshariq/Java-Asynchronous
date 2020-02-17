package com.shariqparwez.asyncexception;

import com.shariqparwez.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ExceptionExample {
    public static void main(String[] args) {
        // ## 1 -  Setting up a Chain That Fails
        //asyncChainExceptionOperationOne();

        // ## 2 -  Setting up the Exceptionally Pattern to Catch an Exception
        //asyncChainExceptionOperationTwo();

        // ## 3 -  Logging an Exception Using the WhenComplete Pattern
        //asyncChainExceptionOperationThree();

        // ## 4 -  Providing a Default Value with the Handle Pattern
        asyncChainExceptionOperationFour();
    }

    private static void asyncChainExceptionOperationOne() {
        // Create a supplier which throws exception
        Supplier<List<Long>> supplyIDs = () -> {
          sleep(200);
          throw new IllegalStateException("No data");
          //return Arrays.asList(1L, 2L, 3L);
        };

        // Create a function which returns list of users as per list of ids fetched from supplier
        Function<List<Long>, List<User>> fetchUsers = ids -> {
            sleep(300);
            return ids.stream().map(User::new).collect(Collectors.toList());
        };

        // Create a consumer which displays list of users fetched from function
        Consumer<List<User>> displayer = users -> users.forEach(System.out::println);

        // Build a completable future from supplier and chain function and consumer
        CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(supplyIDs);
        CompletableFuture<List<User>> fetch = supply.thenApply(fetchUsers);
        CompletableFuture<Void> display = fetch.thenAccept(displayer);

        // Make main thread sleep, which will enable above pipeline of tasks to complete
        sleep(1_000);

        // Display information about each completable future
        System.out.println("Supply : done = " + supply.isDone() +
                " exception = " + supply.isCompletedExceptionally());
        System.out.println("Fetch : done = " + fetch.isDone() +
                " exception = " + fetch.isCompletedExceptionally());
        System.out.println("Display : done = " + display.isDone() +
                " exception = " + display.isCompletedExceptionally());

    }

    private static void asyncChainExceptionOperationTwo() {
        // Create a supplier which throws exception
        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            throw new IllegalStateException("No data");
            //return Arrays.asList(1L, 2L, 3L);
        };

        // Create a function which returns list of users as per list of ids fetched from supplier
        Function<List<Long>, List<User>> fetchUsers = ids -> {
            sleep(300);
            return ids.stream().map(User::new).collect(Collectors.toList());
        };

        // Create a consumer which displays list of users fetched from function
        Consumer<List<User>> displayer = users -> users.forEach(System.out::println);

        // Build a completable future from supplier and chain function and consumer
        CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(supplyIDs);
        CompletableFuture<List<Long>> exception = supply.exceptionally(e -> Arrays.asList());
        CompletableFuture<List<User>> fetch = exception.thenApply(fetchUsers);
        CompletableFuture<Void> display = fetch.thenAccept(displayer);

        // [Additional] When this is uncommented, will throw exception
        //supply.join();

        // Make main thread sleep, which will enable above pipeline of tasks to complete
        sleep(1_000);

        // Display information about each completable future
        System.out.println("Supply : done = " + supply.isDone() +
                " exception = " + supply.isCompletedExceptionally());
        System.out.println("Fetch : done = " + fetch.isDone() +
                " exception = " + fetch.isCompletedExceptionally());
        System.out.println("Display : done = " + display.isDone() +
                " exception = " + display.isCompletedExceptionally());

    }

    private static void asyncChainExceptionOperationThree() {
        // Create a supplier which throws exception
        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            throw new IllegalStateException("No data");
            //return Arrays.asList(1L, 2L, 3L);
        };

        // Create a function which returns list of users as per list of ids fetched from supplier
        Function<List<Long>, List<User>> fetchUsers = ids -> {
            sleep(300);
            return ids.stream().map(User::new).collect(Collectors.toList());
        };

        // Create a consumer which displays list of users fetched from function
        Consumer<List<User>> displayer = users -> users.forEach(System.out::println);

        // Build a completable future from supplier and chain function and consumer
        // Exception handling using whenComplete
        CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(supplyIDs);
        CompletableFuture<List<Long>> exception = supply.whenComplete(
                (ids, e) -> {
                    if (e != null) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                });
        CompletableFuture<List<User>> fetch = exception.thenApply(fetchUsers);
        CompletableFuture<Void> display = fetch.thenAccept(displayer);

        // Make main thread sleep, which will enable above pipeline of tasks to complete
        sleep(1_000);

        // Display information about each completable future
        System.out.println("Supply : done = " + supply.isDone() +
                " exception = " + supply.isCompletedExceptionally());
        System.out.println("Fetch : done = " + fetch.isDone() +
                " exception = " + fetch.isCompletedExceptionally());
        System.out.println("Display : done = " + display.isDone() +
                " exception = " + display.isCompletedExceptionally());

    }

    private static void asyncChainExceptionOperationFour() {
        // Create a supplier which throws exception
        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            throw new IllegalStateException("No data");
            //return Arrays.asList(1L, 2L, 3L);
        };

        // Create a function which returns list of users as per list of ids fetched from supplier
        Function<List<Long>, List<User>> fetchUsers = ids -> {
            sleep(300);
            return ids.stream().map(User::new).collect(Collectors.toList());
        };

        // Create a consumer which displays list of users fetched from function
        Consumer<List<User>> displayer = users -> users.forEach(System.out::println);

        // Build a completable future from supplier and chain function and consumer
        // Exception handling using handle
        CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(supplyIDs);
        CompletableFuture<List<Long>> exception = supply.handle(
                (ids, e) -> {
                    if (e != null) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                        return Arrays.asList();
                    } else {
                        return ids;
                    }
                });
        CompletableFuture<List<User>> fetch = exception.thenApply(fetchUsers);
        CompletableFuture<Void> display = fetch.thenAccept(displayer);

        // Make main thread sleep, which will enable above pipeline of tasks to complete
        sleep(1_000);

        // Display information about each completable future
        System.out.println("Supply : done = " + supply.isDone() +
                " exception = " + supply.isCompletedExceptionally());
        System.out.println("Fetch : done = " + fetch.isDone() +
                " exception = " + fetch.isCompletedExceptionally());
        System.out.println("Display : done = " + display.isDone() +
                " exception = " + display.isCompletedExceptionally());

    }

    private static void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
        }
    }
}
