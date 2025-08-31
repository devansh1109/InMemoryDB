package com.dev;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.atomic.AtomicLong;


public class InMemoryDB {
    private LRUCache cache;
    private Trie trie;
    private final ReentrantReadWriteLock globalLock = new ReentrantReadWriteLock();

    private final AtomicLong hitCount = new AtomicLong(0);
    private final AtomicLong missCount = new AtomicLong(0);
    private final AtomicLong operationCount = new AtomicLong(0);


    public InMemoryDB(int capacity){
        this.cache = new LRUCache(capacity);
        this.trie = new Trie();
    }

    public void set(String key, String value){
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value cannot be null");
        }

        globalLock.writeLock().lock();
        try{
            cache.put(key,value);
            trie.insert(key);
            operationCount.incrementAndGet();
        } finally{
            globalLock.writeLock().unlock();
        }
    }

    public String get(String key){
        if (key == null) return null;
        globalLock.readLock().lock();
        try{
            String value = cache.get(key);
            operationCount.incrementAndGet();
            
            if (value != null) {
                hitCount.incrementAndGet();
            } else {
                missCount.incrementAndGet();
            }
            return value;
        }finally{
            globalLock.readLock().unlock();
        }
    }

    public boolean delete(String key){
        if (key == null) return false;

        globalLock.writeLock().lock();
        try {
            boolean removed = cache.remove(key);
            if (removed) {
                trie.delete(key);
            }
            operationCount.incrementAndGet();
            return removed;
        } finally {
            globalLock.writeLock().unlock();
        }
    }

    public List<String> prefixSearch(String prefix) {
        globalLock.readLock().lock();
        try {
            List<String> trieResults = trie.searchByString(prefix);
            List<String> validResults = new ArrayList<>();
            
            // Only return keys that exist in cache
            for (String key : trieResults) {
                if (cache.get(key) != null) {
                    validResults.add(key);
                }
            }
            operationCount.incrementAndGet();
            return validResults;
        } finally {
            globalLock.readLock().unlock();
        }
    }
    
    public void displayStats() {
        globalLock.readLock().lock();
        try {
            System.out.println("=== Cache Statistics ===");
            System.out.println("Cache size: " + cache.size());
            System.out.println("Cache capacity: " + cache.getCapacity());
            System.out.println("Total operations: " + operationCount.get());
            System.out.println("Cache hits: " + hitCount.get());
            System.out.println("Cache misses: " + missCount.get());
            System.out.println("Hit ratio: " + getHitRatio() + "%");
        } finally {
            globalLock.readLock().unlock();
        }
    }
    
    public double getHitRatio() {
        long hits = hitCount.get();
        long total = hits + missCount.get();
        return total > 0 ? (double) hits / total * 100.0 : 0.0;
    }

}
