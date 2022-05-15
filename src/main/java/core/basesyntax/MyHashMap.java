package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private Node<K, V>[] nodeArray;
    private int size;
    private final static int DEFAULT_CAPACITY = 16;
    private final static float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int newArrayIndex;

    public MyHashMap() {
        this.nodeArray = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        this.threshold = thresholdCalculation(nodeArray.length);
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
        newArrayIndex = findIndex(key);
        Node<K, V> nodeWithNewIndex = nodeArray[newArrayIndex];
        if (Objects.isNull(nodeWithNewIndex)) {
            return null;
        }
        if (Objects.equals(nodeWithNewIndex.key, key)) {
            return nodeWithNewIndex.value;
        }
        if (Objects.nonNull(nodeWithNewIndex.next)) {
            Node<K, V> nextKey = nodeWithNewIndex.next;
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

    private boolean isEmpty(int arrayIndex) {
        return Objects.isNull(nodeArray[arrayIndex]);
    }

    private void reSize() {
        int newArraySize = nodeArray.length * 2;
        Node<K, V>[] newNodeArray = (Node<K, V>[]) new Node[newArraySize];
        thresholdCalculation(newArraySize);
        size = 0;
        Node<K, V>[] oldNodeArray = nodeArray;
        nodeArray = newNodeArray;
        for (Node<K, V> oldNode : oldNodeArray) {
            if (Objects.nonNull(oldNode)) {
                newArrayIndex = findIndex(oldNode.key);
                addNewNode(oldNode.key, oldNode.value, newArrayIndex);
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
        Node<K, V> nodeByIndex = nodeArray[arrayIndex];
        if (isEmpty(arrayIndex)) {
            if (Objects.isNull(key)) {
                nodeArray[arrayIndex] = new Node<>(null, value, 0, null);
                size++;
                return;
            }
            nodeArray[arrayIndex] = new Node<>(key, value, hash(key), null);
            size++;
            return;
        }
        if (Objects.equals(nodeByIndex.key, key)) {
            nodeByIndex.value = value;
            return;
        }
        Node<K, V> nextNode = nodeByIndex.next;
        do {
            if (Objects.isNull(nextNode)) {
                if (Objects.isNull(key)) {
                    nodeByIndex.next = new Node<>(null, value, 0, null);
                    size++;
                    return;
                }
                nodeByIndex.next = new Node<>(key, value, hash(key), null);
                size++;
                return;
            }
            if (Objects.equals(nextNode.key, key)) {
                nextNode.value = value;
                return;
            }
            if (Objects.isNull(nextNode.next)) {
                if (Objects.isNull(key)) {
                    nextNode.next = new Node<>(null, value, 0, null);
                    size++;
                    return;
                }
                nextNode.next = new Node<>(key, value, hash(key), null);
                size++;
                return;
            }
            nextNode = nextNode.next;
        } while (true);
    }

    private int thresholdCalculation(int ArraySize) {
        return threshold = (int) (ArraySize * LOAD_FACTOR);
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
