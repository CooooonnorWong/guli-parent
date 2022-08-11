package com.atguigu.guli.service.cms.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Connor
 * @date 2022/8/9
 */
public class LockDemo {
    private static final Lock LOCK = new ReentrantLock();

    public static void main(String[] args) {
        LockDemo lockDemo = new LockDemo();
        new Thread(lockDemo::ttl, "A").start();
        new Thread(lockDemo::ttl, "B").start();

    }

    public void tl() {
        if (LOCK.tryLock()) {
            try {
                System.out.println(Thread.currentThread().getName() + "\t" + "---come---");
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName() + "\t" + "---leave---");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOCK.unlock();
            }
        } else {
            System.out.println(Thread.currentThread().getName() + "\t" + "抢锁失败,撤离!");
        }
    }

    public void ttl() {
        try {
            if (LOCK.tryLock(2L, TimeUnit.SECONDS)) {
                try {
                    System.out.println(Thread.currentThread().getName() + "\t" + "---come---");
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName() + "\t" + "---leave---");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    LOCK.unlock();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + "\t" + "抢锁失败,撤离!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
