package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private Node<K, V>[] keyArray;
    private int size;
    private final static int MIN_ARRAY_CAPACITY = 16;
    private int threshold;

    public MyHashMap() {
        this.keyArray = (Node<K, V>[]) new Node[MIN_ARRAY_CAPACITY];
        this.size = size;
        this.threshold = (int) (keyArray.length * 0.75);
    }

    @Override
    public void put(K key, V value) {
        int arrayIndex = findIndex(key);
        if (threshold == size) {
            reSize();
        }
        addNewNode(key, value, arrayIndex);
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int newArrayIndex = findIndex(key);
        if (Objects.equals(keyArray[newArrayIndex].key, key)) {
            return keyArray[newArrayIndex].value;
        }
        if (!Objects.equals(keyArray[newArrayIndex].next, null)) {
            Node<K, V> nextKey = keyArray[newArrayIndex].next;
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
        return Objects.equals(keyArray[arrayIndex], null);
    }

    private void reSize() {
        int newArraySize = keyArray.length * 2;
        Node<K, V>[] newKeyArray = (Node<K, V>[]) new Node[newArraySize];
        threshold = (int) (newArraySize * 0.75);
        size = 0;
        Node<K, V>[] oldKeyArray = keyArray;
        keyArray = newKeyArray;
        for (int i = 0; i < oldKeyArray.length; i++) {
            if (Objects.nonNull(oldKeyArray[i])) {
                if (Objects.isNull(oldKeyArray[i].next)) {
                    int newArrayIndex = findIndex(oldKeyArray[i].key);
                    addNewNode(oldKeyArray[i].key, oldKeyArray[i].value, newArrayIndex);
                } else {
                    int newArrayIndex = findIndex(oldKeyArray[i].key);
                    addNewNode(oldKeyArray[i].key, oldKeyArray[i].value, newArrayIndex);
                    Node<K, V> nextNode = oldKeyArray[i].next;
                    while (nextNode != null) {
                        int newNodeIndex = findIndex(nextNode.key);
                        addNewNode(nextNode.key, nextNode.value, newNodeIndex);
                        nextNode = nextNode.next;
                    }
                }
            }
        }
    }

    private int findIndex(K key) {
        if (Objects.isNull(key)) {
            return 0;
        }
        return Math.abs(key.hashCode() % keyArray.length);
    }

    private void addNewNode(K key, V value, int arrayIndex) {

        if (isEmpty(arrayIndex)) {
            if (Objects.isNull(key)) {
                keyArray[arrayIndex] = new Node<>(null, value, 0, null);
                size++;
                return;
            }
            keyArray[arrayIndex] = new Node<>(key, value, key.hashCode(), null);
            size++;
        } else {
            if (Objects.equals(keyArray[arrayIndex].key, key)) {
                keyArray[arrayIndex].value = value;
                return;
            }
            Node<K, V> nextNode = keyArray[arrayIndex].next;
            do {
                if (Objects.isNull(nextNode)) {
                    if (Objects.isNull(key)) {
                        keyArray[arrayIndex].next = new Node<>(null, value, 0, null);
                        size++;
                        return;
                    }
                    keyArray[arrayIndex].next = new Node<>(key, value, key.hashCode(), null);
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
                    nextNode.next = new Node<>(key, value, key.hashCode(), null);
                    size++;
                    return;
                }
                nextNode = nextNode.next;
            } while (true);
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        private Node(K keySet, V valueSet, int hash, Node<K, V> next) {
            this.key = keySet;
            this.value = valueSet;
            this.hash = hash;
            this.next = next;
        }
    }
}
