package com.atguigu.guli.service.cms.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Connor
 * @date 2022/8/9
 */
public class AirCondition {
    private int degree = 0;
    private static final Lock LOCK = new ReentrantLock();
    private static final Condition CONDITION = LOCK.newCondition();

    public void warm() throws InterruptedException {
//        LOCK.lock();
        if (LOCK.tryLock()) {
            try {
                while (degree != 0) {
//                this.wait();
                    CONDITION.await();
                }
                System.out.println(Thread.currentThread().getName() + "\t" + (++degree));
//            this.notifyAll();
                CONDITION.signalAll();
            } finally {
                LOCK.unlock();
            }
        }
    }

    public void cool() throws InterruptedException {
//        LOCK.lock();
        if (LOCK.tryLock()) {
            try {
                while (degree == 0) {
//                this.wait();
                    CONDITION.await();
                }
                System.out.println(Thread.currentThread().getName() + "\t" + (--degree));
//            this.notifyAll();
                CONDITION.signalAll();
            } finally {
                LOCK.unlock();
            }
        }
    }
}
