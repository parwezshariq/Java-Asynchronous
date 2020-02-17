package com.shariqparwez.asyncchain;

import com.shariqparwez.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AsyncExampleMultiBranchEither {
    public static void main(String[] args) {
        // ## 1 -  Displaying the Result of the First Completed Task
        asyncChainOperationOne();
    }

    private static void asyncChainOperationOne() {
        // Create Supplier to supply list of IDs
        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        // Create Function to return list of user (as CompletableFuture) from list of IDs
        Function<List<Long>, CompletableFuture<List<User>>> fetchUsersOne = ids -> {
            sleep(150);
            Supplier<List<User>> userSupplier =
                    () -> ids.stream().map(User::new).collect(Collectors.toList());
            return CompletableFuture.supplyAsync(userSupplier);
        };

        // Create Function to return list of user (as CompletableFuture) from list of IDs
        Function<List<Long>, CompletableFuture<List<User>>> fetchUsersTwo = ids -> {
            sleep(5000);
            Supplier<List<User>> userSupplier =
                    () -> ids.stream().map(User::new).collect(Collectors.toList());
            return CompletableFuture.supplyAsync(userSupplier);
        };

        // Create Consumer to display list of user
        Consumer<List<User>> displayer = users -> users.forEach(System.out::println);

        // Create completable future for supplyIds (supplier) task
        CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);

        // Fetch completable future for usersOne and usersTwo
        CompletableFuture<List<User>> usersOne = completableFuture.thenComposeAsync(fetchUsersOne);
        CompletableFuture<List<User>> usersTwo = completableFuture.thenComposeAsync(fetchUsersTwo);

        // Chain a runnable task to respective user completable futures
        usersOne.thenRun(() -> System.out.println("Users One"));
        usersTwo.thenRun(() -> System.out.println("Users Two"));

        // As one task is complete, consumer should get executed
        usersOne.acceptEither(usersTwo, displayer);

        // Giving the time for asynchronous activities to complete by making main thread to sleep
        sleep(6_000);
    }

    private static void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
        }
    }
}
