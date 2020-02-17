package com.shariqparwez.asyncchain;

import com.shariqparwez.asyncchain.model.Email;
import com.shariqparwez.asyncchain.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AsyncExampleMultiBranch {
    public static void main(String[] args) {
        // ## 1 -  Displaying the Result of Two Tasks in a Third One
        asyncChainOperationOne();
    }

    private static void asyncChainOperationOne() {
        // Create Supplier to supply list of IDs
        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        // Create Function to return list of user (as CompletableFuture) from list of IDs
        Function<List<Long>, CompletableFuture<List<User>>> fetchUsers = ids -> {
            sleep(250);
            Supplier<List<User>> userSupplier =
                    () -> ids.stream().map(User::new).collect(Collectors.toList());
            return CompletableFuture.supplyAsync(userSupplier);
        };

        // Create Function to return list of email (as CompletableFuture) from list of IDs
        Function<List<Long>, CompletableFuture<List<Email>>> fetchEmails = ids -> {
            sleep(350);
            Supplier<List<Email>> userSupplier =
                    () -> ids.stream().map(Email::new).collect(Collectors.toList());
            return CompletableFuture.supplyAsync(userSupplier);
        };

        // Create completable future for supplyIds (supplier) task
        CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);

        // Create completable future for list of users and list of emails
        CompletableFuture<List<User>> userFuture = completableFuture.thenCompose(fetchUsers);
        CompletableFuture<List<Email>> emailFuture = completableFuture.thenCompose(fetchEmails);

        // When both userFuture and emailFuture is complete, then display size of both list
        userFuture.thenAcceptBoth(emailFuture, (users, emails) -> {
            System.out.println(users.size() + " - " + emails.size()) ;
        });

        // Giving the time for asynchronous activities to complete by making main thread to sleep
        sleep(1_000);
    }

    private static void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
        }
    }
}
