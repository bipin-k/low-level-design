package com.github;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {

  private final int capacity;
  private final Map<K, Node<K, V>> cache;
  private final DoublyLinkedList<K, V> dll;

  public LRUCache(int capacity) {
    this.capacity = capacity;
    this.cache = new HashMap<>();
    this.dll = new DoublyLinkedList<>();
  }

  public V get(K key) {
    if (!cache.containsKey(key)) {
      return null;
    }
    Node<K, V> node = cache.get(key);
    dll.moveToHead(node);
    return node.value;
  }

  public void put(K key, V value) {
    if (cache.containsKey(key)) {
      Node<K, V> node = cache.get(key);
      node.value = value;
      dll.moveToHead(node);
    } else {
      if (cache.size() == capacity) {
        Node<K, V> removedNode = dll.removeTail();
        cache.remove(removedNode.key);
      }
      Node<K, V> newNode = new Node<>(key, value);
      dll.addToHead(newNode);
      cache.put(key, newNode);
    }
  }

  // Helper class for doubly linked list node
  private static class Node<K, V> {

    K key;
    V value;
    Node<K, V> prev;
    Node<K, V> next;

    public Node(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }

  // Helper class for doubly linked list
  private static class DoublyLinkedList<K, V> {

    Node<K, V> head;
    Node<K, V> tail;

    public DoublyLinkedList() {
      head = new Node<>(null, null); // dummy head
      tail = new Node<>(null, null); // dummy tail
      head.next = tail;
      tail.prev = head;
    }

    public void addToHead(Node<K, V> node) {
      node.next = head.next;
      node.prev = head;
      head.next.prev = node;
      head.next = node;
    }

    public void moveToHead(Node<K, V> node) {
      removeNode(node);
      addToHead(node);
    }

    public Node<K, V> removeTail() {
      Node<K, V> removedNode = tail.prev;
      removeNode(removedNode);
      return removedNode;
    }

    private void removeNode(Node<K, V> node) {
      node.prev.next = node.next;
      node.next.prev = node.prev;
    }
  }
}
