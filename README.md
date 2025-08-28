# Redis-like In-Memory Cache

A high-performance, Redis-like in-memory cache implementation in Java featuring LRU eviction and prefix search capabilities.

## 🚀 Features

- **LRU Cache**: Efficient Least Recently Used eviction policy
- **Prefix Search**: Fast prefix-based key searching using Trie data structure
- **O(1) Operations**: Fast get/set operations
- **Memory Management**: Configurable capacity with automatic eviction
- **Thread-Safe**: Safe for concurrent operations

## 🏗️ Architecture

- `InMemoryDB`: Main interface for cache operations
- `LRUCache`: Implements LRU eviction using HashMap + Doubly Linked List
- `Trie`: Prefix search functionality
- `TrieNode`: Node structure for the Trie
- `App`: Demo application showcasing features

## 🔧 Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build & Run

```bash
# Clone the repository
git clone https://github.com/yourusername/inmemory-cache.git
cd inmemory-cache

# Compile the project
mvn compile

# Run the demo
mvn exec:java -Dexec.mainClass="com.dev.App"
```

## 💡 Usage

```java
// Create cache with capacity of 100
InMemoryDB cache = new InMemoryDB(100);

// Basic operations
cache.set("user:123", "John Doe");
String user = cache.get("user:123");
boolean deleted = cache.delete("user:123");

// Prefix search
List<String> userKeys = cache.prefixSearch("user:");

// View statistics
cache.displayStats();
```

## 🎯 Use Cases

- **Session Storage**: Web application session management
- **API Caching**: Cache API responses with automatic eviction
- **Configuration Cache**: Store application configuration
- **Rate Limiting**: Track API call rates per user
- **Temporary Data**: Store data with automatic cleanup

## 🛠️ Technical Details

### Time Complexity
- Get: O(1)
- Set: O(1) 
- Delete: O(1)
- Prefix Search: O(p + k) where p = prefix length, k = number of results

### Space Complexity
- O(n) where n = number of stored keys

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 👥 Authors

- Devansh Verma - Initial work

---
⭐ Star this repository if you find it useful!