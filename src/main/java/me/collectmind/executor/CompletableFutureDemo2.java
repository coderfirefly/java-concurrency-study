package me.collectmind.executor;

import java.util.concurrent.CompletableFuture;

/**
 * 串行依赖关系
 *
 * @author monica
 * @date 2020/11/21
 */
public class CompletableFutureDemo2 {

    public static void main(String[] args) {
        CompletableFuture<String> f0 = CompletableFuture.supplyAsync(() -> "Hello World")
                .thenApply(s -> s + " QQ")
                .thenApply(String::toUpperCase);

        System.out.println(f0.join());
    }
}
