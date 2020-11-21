package me.collectmind.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * addOne方法中调用get需要再次获取同一把锁，因为已经获取到了，就可以直接拿到
 *
 * @author monica
 * @date 2020/11/18
 */
public class ReentrantLockDemo {

    public static void main(String[] args) {
        ReentrantLockDemo reentrantLockDemo = new ReentrantLockDemo();
        Thread t1 = new Thread(() -> reentrantLockDemo.addOne());
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main Thread END!");
    }

//    private final Lock rtl = new ReentrantLock();
    private final Lock rtl = new ReentrantLock();
    int value;

    public int get() {
        rtl.lock();
        try {
            return value;
        } finally {
            rtl.unlock();
        }
    }

    public void addOne() {
        rtl.lock();
        try {
            value = 1 + get();
        } finally {
            rtl.unlock();
        }
    }
}
