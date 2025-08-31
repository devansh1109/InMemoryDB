package com.dev;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;

public class ConcurrencyTest {
    public static void main(String[] args) throws InterruptedException {
        InMemoryDB db = new InMemoryDB(1000);
        int numThreads = 10;
        int operationsPerThread = 1000;
        
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        
        System.out.println("Starting concurrency test with " + numThreads + " threads...");
        long startTime = System.currentTimeMillis();
        
        // Launch concurrent threads
       for (int i = 0; i < numThreads; i++) {
            final int threadId = i; // Make thread index final
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        String key = "thread:" + threadId + ":key:" + j;
                        String value = "value:" + j;
                        db.set(key, value);
                        db.get(key);
                        if (j % 10 == 0) {
                            db.prefixSearch("thread:" + threadId);
                        }
                        if (j % 20 == 0) {
                            db.delete(key);
                        }
                        System.out.println("Thread " + threadId + " completed operation " + j);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("\nConcurrency test completed!");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
        db.displayStats();
    }
}