package core.basesyntax;

public interface MyMap<K, V> {
    void put(K key, V value);

    V getValue(K key);

    int getSize();
}
