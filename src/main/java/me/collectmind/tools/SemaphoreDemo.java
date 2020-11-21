package me.collectmind.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 使用信号量实现互斥访问
 *
 * @author monica
 * @date 2020/11/20
 */
public class SemaphoreDemo {

    static int count;
    static final Semaphore s = new Semaphore(1);

    static void addOne() {
        try {
            s.acquire();
            count += 1;
            System.out.println(Thread.currentThread().getName() + " add one, count:" + count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            s.release();
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 10000; i++) {
            executorService.submit(SemaphoreDemo::addOne);
        }
        executorService.shutdown();
        System.out.println("Main Thread");
    }

}
