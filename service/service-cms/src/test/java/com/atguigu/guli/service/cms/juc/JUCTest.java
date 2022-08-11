package com.atguigu.guli.service.cms.juc;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Connor
 * @date 2022/8/8
 */
public class JUCTest {
    private int tickets = 50;
    private final Object lock = new Object();

    public void copyOnWriteArrayList() {
        List<String> list =
                new CopyOnWriteArrayList<>();
//                Collections.synchronizedList(new ArrayList<>());
//                new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 7));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }

    public void copyOnWriteArraySet() {
        Set<String> set =
                new CopyOnWriteArraySet<>();
//                Collections.synchronizedSet(new HashSet<>());
//                new HashSet<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 7));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }

    public void concurrentHashMap() {
        Map<String, String> map =
                new ConcurrentHashMap<>();
//                Collections.synchronizedMap(new HashMap<>());
//                new HashMap<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0, 7));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }
    }

    @Test
    public void testSale() {
        Ticket ticket = new Ticket();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ticket.sale();
//            }
//        }, "A").start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ticket.sale();
//            }
//        }, "B").start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ticket.sale();
//            }
//        }, "C").start();
        Lock lock = new ReentrantLock(true);
        lock.lock();
        try {
            new Thread(ticket::sale, "A").start();
            new Thread(ticket::sale, "B").start();
            new Thread(ticket::sale, "C").start();
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void testLockAndTryLockAndSync() {
        AirCondition airCondition = new AirCondition();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    airCondition.warm();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    airCondition.cool();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    airCondition.cool();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    airCondition.cool();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();

    }

    //测试CopyOnWriteArrayList CopyOnWriteArraySet ConcurrentHashMap
//    public static void main(String[] args) {
////        new JUCTest().copyOnWriteArrayList();
////        new JUCTest().copyOnWriteArraySet();
//        new JUCTest().concurrentHashMap();
//    }

    @Test
    public void testHashCodeConflict() {
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < 200000; i++) {
            int hashCode = new Person(1).hashCode();
            if (set.contains(hashCode)) {
                System.out.println("哈希冲突在" + i + "行");
                continue;
            }
            set.add(hashCode);
        }
        System.out.println(set.size());
    }

    static class Person {
        private int id;

        public Person(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Person) {
                Person person = (Person) obj;
                return this.id == person.id;
            }
            return false;
        }
    }

    @Test
    public void testSequenceSignal() {
        Resource resource = new Resource();
//        FutureTask<String> task1 = new FutureTask<>(() -> {
//            for (int i = 0; i < 10; i++) resource.set1();
//            return String.valueOf(resource.getFlag());
//        });
//        FutureTask<String> task2 = new FutureTask<>(() -> {
//            for (int i = 0; i < 10; i++) resource.set2();
//            return String.valueOf(resource.getFlag());
//        });
//        FutureTask<String> task3 = new FutureTask<>(() -> {
//            for (int i = 0; i < 10; i++) resource.set3();
//            return String.valueOf(resource.getFlag());
//        });
        new Thread(new FutureTask<>(() -> {
            for (int i = 0; i < 10; i++) resource.set1();
            return String.valueOf(resource.getFlag());
        }), "A").start();
        new Thread(new FutureTask<>(() -> {
            for (int i = 0; i < 10; i++) resource.set2();
            return String.valueOf(resource.getFlag());
        }), "B").start();
        new Thread(new FutureTask<>(() -> {
            for (int i = 0; i < 10; i++) resource.set3();
            return String.valueOf(resource.getFlag());
        }), "C").start();

//        System.out.println(task1.get());
//        System.out.println(task2.get());
//        System.out.println(task3.get());
    }

//    static class Resource {
//        private int flag = 1;
//
//        public int getFlag() {
//            return flag;
//        }
//
//        public void setFlag(int flag) {
//            this.flag = flag;
//        }
//
//        private static final Lock LOCK = new ReentrantLock();
//        private static final Condition CONDITION1 = LOCK.newCondition();
//        private static final Condition CONDITION2 = LOCK.newCondition();
//        private static final Condition CONDITION3 = LOCK.newCondition();
//
//        public void set1() {
//            LOCK.lock();
//            try {
//                while (flag != 1) {
//                    CONDITION1.await();
//                }
//                for (int i = 0; i < 5; i++) {
//                    System.out.println(Thread.currentThread().getName() + "\t" + i);
//                }
//                flag = 2;
//                CONDITION2.signal();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                LOCK.unlock();
//            }
//        }
//
//        public void set2() {
//            LOCK.lock();
//            try {
//                while (flag != 2) {
//                    CONDITION2.await();
//                }
//                for (int i = 0; i < 10; i++) {
//                    System.out.println(Thread.currentThread().getName() + "\t" + i);
//                }
//                flag = 3;
//                CONDITION3.signal();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                LOCK.unlock();
//            }
//        }
//
//        public void set3() {
//            LOCK.lock();
//            try {
//                while (flag != 3) {
//                    CONDITION3.await();
//                }
//                for (int i = 0; i < 15; i++) {
//                    System.out.println(Thread.currentThread().getName() + "\t" + i);
//                }
//                flag = 1;
//                CONDITION1.signal();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                LOCK.unlock();
//            }
//        }
//    }

    static class Resource {
        private int flag = 1;

        public int getFlag() {
            return this.flag;
        }

        private static final Lock LOCK = new ReentrantLock();
        private static final Condition CONDITION1 = LOCK.newCondition();
        private static final Condition CONDITION2 = LOCK.newCondition();
        private static final Condition CONDITION3 = LOCK.newCondition();

        public void set1() {
            LOCK.lock();
            try {
                while (flag != 1) {
                    CONDITION1.await();
                }
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                }
                flag = 2;
                CONDITION2.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOCK.unlock();
            }
        }

        public void set2() {
            LOCK.lock();
            try {
                while (flag != 2) {
                    CONDITION2.await();
                }
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                }
                flag = 3;
                CONDITION3.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOCK.unlock();
            }

        }

        public void set3() {
            LOCK.lock();
            try {
                while (flag != 3) {
                    CONDITION3.await();
                }
                for (int i = 0; i < 15; i++) {
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                }
                flag = 1;
                CONDITION1.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOCK.unlock();
            }

        }
    }

    @Test
    public void testCountDownLatch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(new FutureTask<>(() -> {
                System.out.println(Thread.currentThread().getName() + "\t" + finalI);
                countDownLatch.countDown();
                return Thread.currentThread().getName();
            }), String.valueOf(i)).start();
        }
        countDownLatch.await();
        System.out.println("------OVER------");
    }

    @Test
    public void testCyclicBarrier() throws BrokenBarrierException, InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> System.out.println("------OVER------"));
        for (int i = 0; i < 7; i++) {
            int finalI = i;
            new Thread(new FutureTask<>(() -> {
                System.out.println(Thread.currentThread().getName() + "\t" + finalI);
                cyclicBarrier.await();
                return Thread.currentThread().getName();
            }), String.valueOf(i)).start();
        }
    }

    //    @Test
    //测试 Semaphore
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2);
        for (int i = 0; i < 10; i++) {
            new Thread(new FutureTask<>(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "\t" + "抢到车位");
                    TimeUnit.SECONDS.sleep(new Random().nextInt(3) + 1);
                    System.out.println(Thread.currentThread().getName() + "\t" + "出库");
                } finally {
                    semaphore.release();
                }
                return Thread.currentThread().getName();
            }), String.valueOf(i)).start();
        }
    }

    @Test
    public void testCAS() {
        AtomicInteger i = new AtomicInteger(1);
        System.out.println("i的初始值 = " + i.get());
        System.out.println("第一次修改i为10 = " + i.compareAndSet(1, 10));
        System.out.println("第一次修改后的i = " + i.get());
        System.out.println("第二次修改i为10 = " + i.compareAndSet(1, 100));
        System.out.println("第二次修改后的i = " + i.get());
    }

    static class CASDemo {
        private AtomicInteger i = new AtomicInteger();

        public int getI() {
            return i.get();
        }

        public void setI(int expect, int update) {
            this.i.compareAndSet(expect, update);
        }

        public static void main(String[] args) throws InterruptedException {
            CASDemo cas = new CASDemo();
            int limit = 100;
            CountDownLatch countDownLatch = new CountDownLatch(limit);
            CyclicBarrier cyclicBarrier = new CyclicBarrier(limit, () -> System.out.println("最后的修改结果为: i = " + cas.getI()));
            Semaphore semaphore = new Semaphore(limit);
            for (int i = 0; i < limit; i++) {
                int finalI = i;
                new Thread(new FutureTask<String>(() -> {
                    try {
                        semaphore.acquire();
                        cas.setI(cas.getI(), finalI);
//                        cyclicBarrier.await();
//                        countDownLatch.countDown();
                        return Thread.currentThread().getName();
                    } finally {
                        semaphore.release();
                    }
                }), "A").start();
            }

//            countDownLatch.await();
            System.out.println("最后的修改结果为: i = " + cas.getI());
        }
    }
}

