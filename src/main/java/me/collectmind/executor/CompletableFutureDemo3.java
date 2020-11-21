package me.collectmind.executor;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author monica
 * @date 2020/11/21
 */
public class CompletableFutureDemo3 {

    public static void main(String[] args) {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            int t = new Random().nextInt(10) + 5;
            sleep(t, TimeUnit.SECONDS);
            return String.valueOf(t);
        });

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            int t = new Random().nextInt(10) + 5;
            sleep(t, TimeUnit.SECONDS);
            return String.valueOf(t);
        });

        CompletableFuture<String> f3 = f1.applyToEither(f2, s -> s);

        System.out.println(f3.join());
    }

    private static void sleep(int t, TimeUnit u) {
        try {
            u.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
