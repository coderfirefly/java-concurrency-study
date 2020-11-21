package me.collectmind.executor;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Fork/Join框架示例
 * @author monica
 * @date 2020/11/21
 */
public class ForkJoinDemo {

    public static void main(String[] args) {
        // 创建一个分治任务的线程池
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        // 创建一个分治任务
        Fibonacci fib = new Fibonacci(30);
        // 启动分治任务
        Integer result = forkJoinPool.invoke(fib);
        System.out.println(result);
    }


    static class Fibonacci extends RecursiveTask<Integer> {
        final int n;

        Fibonacci(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            if (n <= 1) {
                return n;
            }

            Fibonacci f1 = new Fibonacci(n - 1);
            // 异步执行子任务
            f1.fork();

            Fibonacci f2 = new Fibonacci(n - 2);
            return f2.compute() + f1.join();
        }
    }
}
