package me.collectmind.locks;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 活锁的例子
 * 线程非阻塞的获取锁，都先拿到自己的锁，想要对方的锁；
 * 条件不满足，同时释放锁；
 * while true 重复上述获取锁的过程
 *
 * 解决方法：内层获取锁时加入随机等待时间
 *
 * @author monica
 * @date 2020/11/18
 */
public class Account {

    // 账户余额
    private int balance;
    // 锁
    private final Lock lock = new ReentrantLock();

    public Account(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public static void main(String[] args) {
        Account a = new Account(100);
        Account b = new Account(300);

        Thread t1 = new Thread(() -> a.transfer(b, 100));
        Thread t2 = new Thread(() -> b.transfer(a, 100));
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("a:" + a.getBalance() + ", b:" + b.getBalance());
        System.out.println("Main Thread End");
    }

    void transfer(Account tar, int amt) {
        while (true) {
            if (this.lock.tryLock()) {
                System.out.println(Thread.currentThread().getName() + " get this lock");
                try {
                    if (tar.lock.tryLock(new Random().nextInt(10), TimeUnit.SECONDS)) {
                        System.out.println(Thread.currentThread().getName() + " get tar lock");
                        try {
                            this.balance -= amt;
                            tar.balance += amt;
                            break;
                        } finally {
                            tar.lock.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    this.lock.unlock();
                }
            }
        }
    }
}
