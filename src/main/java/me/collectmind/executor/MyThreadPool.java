package me.collectmind.executor;

import java.nio.file.Watchable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by monica on 2020/11/21.
 */
public class MyThreadPool {

    /**
     * 提交给线程池的任务都放到一个阻塞队列里
     */
    BlockingQueue<Runnable> workQueue;

    /**
     * 保存内部的工作线程
     */
    List<WorkerThread> threads = new ArrayList<>();


    public MyThreadPool(int poolSize, BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        for (int i = 0; i < poolSize; i++) {
            WorkerThread workerThread = new WorkerThread();
            workerThread.start();
            threads.add(workerThread);
        }
    }

    /**
     * 将任务提交给线程池（放到阻塞队列里面）
     *
     * @param command
     */
    public void execute(Runnable command) {
        try {
            workQueue.put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 内部类，
     * 工作线程负责从阻塞队列中取出任务，执行任务
     */
    class WorkerThread extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Runnable task = workQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>(2);
        MyThreadPool pool = new MyThreadPool(10, workQueue);
        for (int i = 0; i < 100; i++) {
            pool.execute(() -> System.out.println("hello " + Thread.currentThread().getName()));
        }
    }
}
