package me.collectmind.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LOCK 如何保证并发编程中的可见性问题
 *
 * @author monica
 * @date 2020/11/18
 */
public class LockCountDemo {

    private final Lock rtl = new ReentrantLock();
    private int value;

    public static void main(String[] args) {
        LockCountDemo lockDemo = new LockCountDemo();
        Thread t1 = new Thread(() -> lockDemo.addOne());
        Thread t2 = new Thread(() -> lockDemo.addOne());
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main Thread End!");
    }

    public void addOne() {
        rtl.lock();
        try {
            value += 1;
            System.out.println(Thread.currentThread().getName() + " add One, value:" + value);
        } finally {
            rtl.unlock();
        }
    }
}
/**
 * TODO：有疑问 需要看AQS源码理解
 *
 * 输出结果：
 * Thread-0 add One, value:1
 * Thread-1 add One, value:2
 * Main Thread End!
 *
 * 线程t0的 value+=1 happens before t0的解锁；
 * 线程t0的解锁 happens before t1的加锁；
 * 线程t0的value+=1 happens before t1 的加锁；（传递性规则）
 */
