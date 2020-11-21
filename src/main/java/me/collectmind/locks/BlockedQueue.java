package me.collectmind.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用条件变量实现阻塞队列（类似 ArrayBlockingQueue ）
 *
 * @author monica
 * @date 2020/11/18
 */
public class BlockedQueue<T> {
    /**
     * 锁和两个条件变量
     */
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    /**
     * 队列元素
     */
    private final Object[] items;
    // 数据个数，当前可存放的数组下标，当前可取的下标
    /**
     * 当前队列元素个数
     * 当前入队列的元素下标
     * 当前出队列的元素下标
     */
    int count, enqPtr, deqPtr;

    public BlockedQueue(int capacity) {
        this.items = new Object[capacity];
    }

    /**
     * 元素入队列
     *
     * @param x
     */
    public void enq(T x) {
        lock.lock();
        try {
            while (this.items.length == count) {
                // 队列已满时等待队列不满的条件
                notFull.await();
            }

            // 入队列
            this.items[enqPtr] = x;
            this.count++;

            if (++enqPtr == this.items.length) {
                enqPtr = 0;
            }

            // 通知队列不空条件的等待线程，表示可以出队
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    /**
     * 出队列
     * @return
     */
    public T deq() {
        lock.lock();
        try {
            while (count == 0) {
                // 队列为空时，等待队列不空的条件
                notEmpty.await();
            }

            // 元素出队列
            T x = (T) this.items[deqPtr];
            if (++deqPtr == this.items.length) {
                deqPtr = 0;
            }
            this.count--;

            // 通知等待队列不满条件的线程，表示可以入队
            notFull.signal();
            return x;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {
        ExecutorService producer = Executors.newSingleThreadExecutor();
        ExecutorService consumer = Executors.newSingleThreadExecutor();
        BlockedQueue<Integer> blockedQueue = new BlockedQueue(10);

        producer.execute(() -> {
            try {
                for (int i = 0; i < 30; i++) {
                    blockedQueue.enq(i);
                    System.out.println("Enq >>> " + i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        consumer.execute(() -> {
            try {
                for (int i = 0; i < 30; i++) {
                    Object object = blockedQueue.deq();
                    System.out.println("Deq >>> " + object);
                    Thread.sleep(200L);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        producer.shutdown();
        consumer.shutdown();
        System.out.println("Main Thread End!");
    }
}
