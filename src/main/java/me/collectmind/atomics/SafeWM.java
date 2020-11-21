package me.collectmind.atomics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 对象引用原子类实例
 *
 * @author monica
 * @date 2020/11/21
 */
public class SafeWM {

    class WMRange {
        final  int upper;
        final  int lower;

        public WMRange(int upper, int lower) {
            this.upper = upper;
            this.lower = lower;
        }
    }


    final AtomicReference<WMRange> rf = new AtomicReference<>(new WMRange(0, 0));

    public void setUpper(int v) {
        WMRange newWm;
        WMRange oldWm;
        do {
            // 必须放在循环体内，否则会造成死循环
            oldWm = rf.get();
            if (v < oldWm.lower) {
                throw new IllegalArgumentException();
            }
            newWm = new WMRange(v, oldWm.lower);
        } while (!rf.compareAndSet(oldWm, newWm));

        System.out.println("set uppper v:"  + v);
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        SafeWM safeWm = new SafeWM();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            executorService.submit(() -> safeWm.setUpper(finalI));
        }
        executorService.shutdown();
        System.out.println("Main Thread End!");
    }
}
