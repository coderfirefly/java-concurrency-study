package me.collectmind.tools;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * 使用信号量实现限流器
 *
 * @author monica
 * @date 2020/11/20
 */
public class ObjPool<T, R> {

    /**
     * 对象池
     */
    final List<T> pool;
    /**
     * 信号量控制访问线程数
     */
    final Semaphore sem;

    /**
     * 构造器
     * @param size 对象的个数
     * @param t 对象
     */
    public ObjPool(int size, T t) {
        this.pool = new Vector<>();
        for (int i = 0; i < size; i++) {
            pool.add(t);
        }
        this.sem = new Semaphore(size);
    }

    public R exec(Function<T, R> function) {
        T t = null;
        try {
            this.sem.acquire();
            // 获取对象池中的一个对象
            t = pool.remove(0);
            return function.apply(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            // 使用完毕后放入对象池
            pool.add(t);
            this.sem.release();
        }
    }

    public static void main(String[] args) {
        ObjPool<Integer, String> pool = new ObjPool<>(10, 2);
        ExecutorService executorService = Executors.newFixedThreadPool(16);

        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> pool.exec(t -> {
                System.out.println(t);
                return t.toString();
            }));
        }
        executorService.shutdown();
        System.out.println("Main Thread End!");
    }
}
