package com.shariqparwez.performant;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpClientExample {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        // ## 1 -  Setting up the HttpClient Example
        //asyncPerformantOperationOne();

        // ## 2 -  Running the HttpClient Example through completableFuture
        //asyncPerformantOperationTwo();

        // ## 3 -  Chaining the tasks and Controlling the Threads
        //asyncPerformantOperationThree();

        // ## 4 -  CDelaying the Start of the HttpClient Example
        asyncPerformantOperationFour();
    }

    private static void asyncPerformantOperationOne() throws IOException, InterruptedException {
        // Create HttpClient
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        // Build request
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://www.amazon.com"))
                .build();

        // Fetch response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Store length of the response, to validate if indeed response was fetched
        int length = response.body().length();

        // Print response, i.e. length
        System.out.println("Length = " + length);
    }

    private static void asyncPerformantOperationTwo() throws IOException, InterruptedException, ExecutionException {
        // Create HttpClient
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        // Build request
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://www.amazon.com"))
                .build();

        // Fetch response as completable future, by using client.sendAsync
        CompletableFuture<HttpResponse<String>> future =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        // Store length of the response, to validate if indeed response was fetched
        // Fetch response body from completableFuture object
        //int length = future.get().body().length();
        // OR
        int length = future.join().body().length();

        // Print response, i.e. length
        System.out.println("Length = " + length);
    }

    private static void asyncPerformantOperationThree() throws IOException, InterruptedException, ExecutionException {
        // Create executor with thread pool of single thread
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create HttpClient
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        // Build request
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://www.amazon.com"))
                .build();

        // Fetch response as completable future, by using client.sendAsync
        CompletableFuture<HttpResponse<String>> future =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        // Chain further tasks
        future.thenAcceptAsync(
                response -> {
                    String body = response.body();
                    System.out.println("body = " + body.length() + " [" + Thread.currentThread().getName() + "]");
                }, executor)
                .thenRun(() -> System.out.println("Done!"))
                .join();

        // Terminate JVM post completion
        executor.shutdown();
    }

    private static void asyncPerformantOperationFour() throws IOException, InterruptedException, ExecutionException {
        // Create executor with thread pool of single thread
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create HttpClient
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        // Build request
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://www.amazon.com"))
                .build();

        // Create a completable future object using constructor
        CompletableFuture<Void> start = new CompletableFuture<>();

        // Fetch response as completable future, by using completable future 'start'
        CompletableFuture<HttpResponse<String>> future =
                start.thenCompose(nil -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString()));

        // Chain further tasks
        future.thenAcceptAsync(
                response -> {
                    String body = response.body();
                    System.out.println("body = " + body.length() + " [" + Thread.currentThread().getName() + "]");
                }, executor)
                .thenRun(() -> System.out.println("Done!"));

        // Complete the task 'start' completableFuture
        start.complete(null);

        // Make main thread sleep, for completion of async execution
        Thread.sleep(3_000);

        // Terminate JVM post completion
        executor.shutdown();
    }
}
