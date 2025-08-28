package com.dev;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDB {
    private LRUCache cache;
    private Trie trie;

    public InMemoryDB(int capacity){
        this.cache = new LRUCache(capacity);
        this.trie = new Trie();
    }

    public void set(String key, String value){
        cache.put(key,value);
        trie.insert(key);
    }

    public String get(String key){
        return cache.get(key);
    }

    public boolean delete(String key){
        boolean removed = cache.remove(key);
        if(removed) trie.delete(key);
        return removed;
    }

    public List<String> prefixSearch(String prefix){
        List<String> trieResults = trie.searchByString(prefix);
        List<String> validResults = new ArrayList<>();
        
        // Only return keys that exist in cache
        for(String key : trieResults) {
            if(cache.get(key) != null) {
                validResults.add(key);
            }
        }
        return validResults;
    }
    
    public void displayStats(){
        System.out.println("Cache size:"+ cache.size());
        System.out.println("Cache capacity:"+ cache.getCapacity());
    }

}
