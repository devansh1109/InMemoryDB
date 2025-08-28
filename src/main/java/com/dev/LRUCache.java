package com.dev;
import java.util.HashMap;
import java.util.Map;

public class LRUCache {
    private int capacity;
    private Map<String,Node> cache;
    private Node head,tail;    

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
        if(!cache.containsKey(key)) return null;
        Node node = cache.get(key);
        removeNode(node);
        insertToTail(node);
        return node.value;
    }

    public void put(String key, String value){
        if(cache.containsKey(key)){
            Node existingNode = cache.get(key);
            existingNode.value = value;  // Update existing value
            removeNode(existingNode);
            insertToTail(existingNode);
            return;
        }
        
        Node newnode = new Node(key,value);
        insertToTail(newnode);
        cache.put(key,newnode);

        if(cache.size() > capacity){
            Node lru = head.next;
            removeNode(lru);
            cache.remove(lru.key);
        }
    }

    public boolean remove(String key) {
        if (!cache.containsKey(key)) {
            return false;
        }
        Node node = cache.get(key);
        removeNode(node);
        cache.remove(key);
        return true;
    }

    public int size() {
        return cache.size();
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
