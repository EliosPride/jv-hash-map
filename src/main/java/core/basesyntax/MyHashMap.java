package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private final static int DEFAULT_CAPACITY = 16;
    private final static float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] nodeArray;
    private int size;
    int threshold;

    public MyHashMap() {
        this.nodeArray = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        this.threshold = calculateThreshold(nodeArray.length);
    }

    @Override
    public void put(K key, V value) {
        int index = findIndex(key);
        if (threshold == size) {
            reSize();
        }
        addNewNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int nodeIndex = findIndex(key);
        Node<K, V> existedNode = nodeArray[nodeIndex];
        if (Objects.isNull(existedNode)) {
            return null;
        }
        if (Objects.equals(existedNode.key, key)) {
            return existedNode.value;
        }
        if (Objects.nonNull(existedNode.next)) {
            Node<K, V> nextKey = existedNode.next;
            while (nextKey != null) {
                if (Objects.equals(nextKey.key, key)) {
                    return nextKey.value;
                }
                nextKey = nextKey.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void reSize() {
        int newArraySize = nodeArray.length * 2;
        Node<K, V>[] newNodeArray = (Node<K, V>[]) new Node[newArraySize];
        calculateThreshold(newArraySize);
        size = 0;
        Node<K, V>[] oldNodeArray = nodeArray;
        nodeArray = newNodeArray;
        for (Node<K, V> oldNode : oldNodeArray) {
            if (Objects.nonNull(oldNode)) {
                int nodeIndex = findIndex(oldNode.key);
                addNewNode(oldNode.key, oldNode.value, nodeIndex);
                Node<K, V> nextNode = oldNode.next;
                while (nextNode != null) {
                    int newNodeIndex = findIndex(nextNode.key);
                    addNewNode(nextNode.key, nextNode.value, newNodeIndex);
                    nextNode = nextNode.next;
                }
            }
        }
    }

    private int findIndex(K key) {
        return Objects.isNull(key) ? 0 : Math.abs(hash(key) % nodeArray.length);
    }

    private void addNewNode(K key, V value, int arrayIndex) {
        Node<K, V> existedNode = nodeArray[arrayIndex];
        if (Objects.isNull(nodeArray[arrayIndex])) {
            nodeArray[arrayIndex] = new Node<>(key, value, hash(key), null);
            size++;
            return;
        }
        if (Objects.equals(existedNode.key, key)) {
            existedNode.value = value;
            return;
        }
        Node<K, V> nextNode = existedNode.next;
        do {
            if (Objects.isNull(nextNode)) {
                existedNode.next = new Node<>(key, value, hash(key), null);
                size++;
                return;
            }
            if (Objects.equals(nextNode.key, key)) {
                nextNode.value = value;
                return;
            }
            if (Objects.isNull(nextNode.next)) {
                nextNode.next = new Node<>(key, value, hash(key), null);
                size++;
                return;
            }
            nextNode = nextNode.next;
        } while (true);
    }

    private int calculateThreshold(int size) {
        threshold = (int) (size * LOAD_FACTOR);
        return threshold;
    }

    private int hash(K key) {
        return Objects.isNull(key) ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        private Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }
}
