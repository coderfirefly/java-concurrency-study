package me.collectmind.locks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用读写锁实现一个缓存工具类
 *
 *
 * @author monica
 * @date 2020/11/20
 */
public class Cache<K, V> {
    final Map<K, V> m = new HashMap<>();
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock rLock = rwLock.readLock();
    private final Lock wLock = rwLock.writeLock();

    public V get(K key) {
        rLock.lock();
        try {
            return m.get(key);
        } finally {
            rLock.unlock();
        }
    }

    public void put(K key, V value) {
        wLock.lock();
        try {
            m.put(key, value);
        } finally {
            wLock.unlock();
        }
    }
}
