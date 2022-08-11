package com.atguigu.guli.service.cms.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Connor
 * @date 2022/8/8
 */
public class Ticket {
    private int ticketCount = 50;
    private Lock lock = new ReentrantLock();

//    public synchronized void sale() {
//        while (ticketCount > 0) {
//            System.out.println(Thread.currentThread().getName() + ":售出了第" + ticketCount-- + "张票,还剩" + ticketCount + "张票");
//        }
//    }

//    public void sale() {
//        lock.lock();
//        try {
//            while (ticketCount > 0) {
//                System.out.println(Thread.currentThread().getName() + ":售出了第" + ticketCount-- + "张票,还剩" + ticketCount + "张票");
//            }
//        } finally {
//            lock.unlock();
//        }
//    }

    public void sale() {
        while (ticketCount > 0) {
            System.out.println(Thread.currentThread().getName() + ":售出了第" + ticketCount-- + "张票,还剩" + ticketCount + "张票");
        }

    }
}
