package me.collectmind.locks;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock 示例
 *
 * @author monica
 * @date 2020/11/20
 */
public class Person {

    private int x, y;
    final StampedLock sLock = new StampedLock();

    public double distanceFromOrigin() {
        // 乐观读（无锁）
        long stamp = sLock.tryOptimisticRead();
        // 读取共享变量
        int curX = this.x, curY = this.y;

        // 判断在读取的过程中数据是否被修改
        if (!sLock.validate(stamp)) {
            // 锁升级为悲观读锁
            stamp = sLock.readLock();
            try {
                curX = this.x;
                curY = this.y;
            } finally {
                sLock.unlockRead(stamp);
            }
            return  Math.sqrt(curX * curX + curY * curY);
        }
        return 0;
    }

    public static void main(String[] args) {

    }
}
