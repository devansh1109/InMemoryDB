package com.dev;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    private Map<Character,TrieNode> children;
    private boolean isEndOfWord;
    private String key;

    public TrieNode(){
        this.children = new HashMap<>();
        this.isEndOfWord = false;
        this.key = null;
    }

    public Map<Character,TrieNode> getChildren(){
        return children;
    }

    public boolean isEndOfWord(){
        return isEndOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        this.isEndOfWord = endOfWord;
    }

    public String getKey(){
        return key;
    }

    public void setKey(String key){
        this.key = key;
    }

    public boolean hasChild(char ch) {
        return children.containsKey(ch);
    }
    
    public TrieNode getChild(char ch) {
        return children.get(ch);
    }
    
    public void putChild(char ch, TrieNode node) {
        children.put(ch, node);
    }
    
    public void removeChild(char ch) {
        children.remove(ch);
    }
    
    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
