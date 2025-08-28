package com.dev;
import java.util.ArrayList;
import java.util.List;

public class Trie {
    private TrieNode root;

    public Trie(){
        this.root = new TrieNode();
    }

    public void insert(String key){
        if(key == null || key.isEmpty()) return;

        TrieNode current = root;
        for(char ch: key.toCharArray()){
            if(!current.hasChild(ch)){
                current.putChild(ch, new TrieNode());
            }
            current = current.getChild(ch);
        }
        current.setEndOfWord(true);
        current.setKey(key);
    }

    public boolean search(String key){
        if(key == null || key.isEmpty()) return false;

        TrieNode current = root;
        for(char ch: key.toCharArray()){
            if(!current.hasChild(ch)){
                return false;
            }
            current= current.getChild(ch);
        }
        return current.isEndOfWord();
    }

    public boolean delete(String key) {
        if (key == null || key.isEmpty()) return false;
        return deleteHelper(root, key, 0);
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
        
        TrieNode current = root;
        
        for (char ch : prefix.toCharArray()) {
            if (!current.hasChild(ch)) {
                return result; 
            }
            current = current.getChild(ch);
        }
        
        collectWords(current, result);
        return result;
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
