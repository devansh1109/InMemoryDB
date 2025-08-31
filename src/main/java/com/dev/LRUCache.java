package com.dev;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache {
    private int capacity;
    private Map<String,Node> cache;
    private Node head,tail; 
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
   

    private class Node {
        String key;
        String value;
        Node prev;
        Node next;
        
        Node(String key, String value){
            this.key = key;
            this.value = value;
        }
    }

    public LRUCache(int capacity){
        this.capacity = capacity;
        this.cache = new HashMap<>();

        this.head = new Node("","");
        this.tail = new Node("", "");
        head.next = tail;
        tail.prev = head;
    }

    public String get(String key){
        lock.writeLock().lock();
        try{
            Node node = cache.get(key);
            if(node == null) return null;
            removeNode(node);
            insertToTail(node);
            return node.value;
        }finally{
            lock.writeLock().unlock();
        }
    }
    public void put(String key, String value){
        lock.writeLock().lock();
        try{
            if(cache.containsKey(key)){
                Node existingNode = cache.get(key);
                existingNode.value = value;  // Update value
                removeNode(existingNode);
                insertToTail(existingNode);
            } else {
                Node newNode = new Node(key, value);
                insertToTail(newNode);
                cache.put(key, newNode);

                if(cache.size() > capacity){
                    Node lru = head.next;
                    removeNode(lru);
                    cache.remove(lru.key);
                }
            }
        }finally{
            lock.writeLock().unlock();
        }
    }

    public boolean remove(String key) {
        lock.writeLock().lock();
        try{
            if (!cache.containsKey(key)) {
                return false;
            }
            Node node = cache.get(key);
            removeNode(node);
            cache.remove(key);
            return true;
        }finally{
            lock.writeLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try{
            return cache.size();
        }finally{
            lock.readLock().unlock();
        }
    }

    public int getCapacity() {
        return capacity;
    }

    private void removeNode(Node node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void insertToTail(Node node){
        node.prev = tail.prev;
        node.next = tail;
        tail.prev.next = node;
        tail.prev = node;
    }
}
