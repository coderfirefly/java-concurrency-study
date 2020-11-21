package me.collectmind.executor;

import java.util.concurrent.CompletableFuture;

/**
 * 处理异常的例子
 * @author monica
 * @date 2020/11/21
 */
public class CompletableFutureDemo4 {

    public static void main(String[] args) {
        CompletableFuture<Integer> f0 = CompletableFuture.supplyAsync(() -> 7/0)
                .thenApply(r -> r * 10)
                .exceptionally(e -> 0);
        System.out.println(f0.join());
    }
}
