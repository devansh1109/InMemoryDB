package com.dev;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Trie {
    private TrieNode root;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Trie(){
        this.root = new TrieNode();
    }

    public void insert(String key){
        if(key == null || key.isEmpty()) return;

        lock.writeLock().lock();
        try{
            TrieNode current = root;
            for(char ch: key.toCharArray()){
                if(!current.hasChild(ch)){
                    current.putChild(ch, new TrieNode());
                }
                current = current.getChild(ch);
            }
            current.setEndOfWord(true);
            current.setKey(key);
        }finally{
            lock.writeLock().unlock();
        }
    }

    public boolean search(String key){
        if(key == null || key.isEmpty()) return false;

        lock.readLock().lock();
        try{
            TrieNode current = root;
            for(char ch: key.toCharArray()){
                if(!current.hasChild(ch)){
                    return false;
                }
                current= current.getChild(ch);
            }
            return current.isEndOfWord();
        }finally{
            lock.readLock().unlock();
        }
    }

    public boolean delete(String key) {
        if (key == null || key.isEmpty()) return false;
        lock.writeLock().lock();
        try{
            return deleteHelper(root, key, 0);
        }finally{
            lock.writeLock().unlock();
        }
    }

    private boolean deleteHelper(TrieNode node, String key, int index) {
        if (index == key.length()) {
            if (!node.isEndOfWord()) return false;
            
            node.setEndOfWord(false);
            node.setKey(null);
            
            return !node.hasChildren();
        }
        
        char ch = key.charAt(index);
        TrieNode child = node.getChild(ch);
        
        if (child == null) return false;
        
        boolean shouldDeleteChild = deleteHelper(child, key, index + 1);
        
        if (shouldDeleteChild) {
            node.removeChild(ch);
            return !node.isEndOfWord() && !node.hasChildren();
        }
        return false;
    }

    public List<String> searchByString(String prefix) {
        List<String> result = new ArrayList<>();
        if (prefix == null) return result;
        lock.readLock().lock();
        try{
            TrieNode current = root;
            
            for (char ch : prefix.toCharArray()) {
                if (!current.hasChild(ch)) {
                    return result; 
                }
                current = current.getChild(ch);
            }
            
            collectWords(current, result);
            return result;
        }finally{
            lock.readLock().unlock();
        }
    }

    private void collectWords(TrieNode node, List<String> result) {
        if (node.isEndOfWord()) {
            result.add(node.getKey());
        }
        
        for (TrieNode child : node.getChildren().values()) {
            collectWords(child, result);
        }
    }

    public boolean startsWith(String prefix) {
        if (prefix == null || prefix.isEmpty()) return true;
        
        TrieNode current = root;
        for (char ch : prefix.toCharArray()) {
            if (!current.hasChild(ch)) {
                return false;
            }
            current = current.getChild(ch);
        }
        return true;
    }

}
