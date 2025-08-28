package com.dev;

import java.util.List;

public class App {
    public static void main(String[] args) {
        // Entry point for the Redis-like in-memory cache
        System.out.println("Starting In-Memory Cache Application...");
        System.out.println("=========================================");
        
        // Initialize InMemoryDB with capacity of 5
        InMemoryDB db = new InMemoryDB(5);
        
        // Demonstrate basic operations
        System.out.println("\n--- Basic Operations ---");
        db.set("user:123", "John Doe");
        db.set("user:456", "Jane Smith");
        db.set("session:abc", "active");
        db.set("session:def", "inactive");
        db.set("config:timeout", "300");
        
        System.out.println("Get user:123: " + db.get("user:123"));
        System.out.println("Get session:abc: " + db.get("session:abc"));
        System.out.println("Get non-existent: " + db.get("non-existent"));
        
        // Display current stats
        System.out.println("\n--- Cache Stats ---");
        db.displayStats();
        
        // Demonstrate LRU eviction
        System.out.println("\n--- Testing LRU Eviction ---");
        System.out.println("Adding one more item to exceed capacity...");
        db.set("product:789", "Phone");
        db.displayStats();
        
        // Check if oldest item was evicted
        System.out.println("Checking if oldest item was evicted:");
        System.out.println("Get user:123: " + db.get("user:123"));
        
        // Demonstrate prefix search
        System.out.println("\n--- Prefix Search ---");
        List<String> userKeys = db.prefixSearch("user:");
        System.out.println("Keys with prefix 'user:': " + userKeys);
        
        List<String> sessionKeys = db.prefixSearch("session:");
        System.out.println("Keys with prefix 'session:': " + sessionKeys);
        
        List<String> configKeys = db.prefixSearch("config:");
        System.out.println("Keys with prefix 'config:': " + configKeys);
        
        // Demonstrate deletion
        System.out.println("\n--- Delete Operations ---");
        boolean deleted = db.delete("session:def");
        System.out.println("Deleted session:def: " + deleted);
        
        // Try to delete non-existent key
        boolean deletedNonExistent = db.delete("non-existent");
        System.out.println("Deleted non-existent: " + deletedNonExistent);
        
        // Check prefix search after deletion
        List<String> sessionKeysAfterDelete = db.prefixSearch("session:");
        System.out.println("Session keys after deletion: " + sessionKeysAfterDelete);
        
        // Final stats
        System.out.println("\n--- Final Cache Stats ---");
        db.displayStats();
        
        // Demonstrate cache access patterns (LRU behavior)
        System.out.println("\n--- LRU Access Pattern Demo ---");
        db.get("user:456"); // Make this recently used
        db.set("new:item1", "value1");
        db.set("new:item2", "value2"); // This should trigger eviction
        
        System.out.println("After adding new items:");
        db.displayStats();
        
        // Show all remaining keys
        System.out.println("\nAll keys in cache (via prefix search with empty string):");
        List<String> allKeys = db.prefixSearch("");
        for (String key : allKeys) {
            System.out.println("Key: " + key + " -> Value: " + db.get(key));
        }
        
        System.out.println("\n=========================================");
        System.out.println("In-Memory Cache Application Completed!");
    }
}