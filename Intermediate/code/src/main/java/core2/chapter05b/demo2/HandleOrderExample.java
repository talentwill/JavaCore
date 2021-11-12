package core2.chapter05b.demo2;

import java.util.concurrent.locks.ReentrantReadWriteLock;

class HandleOrderExample {
    private static final ReentrantReadWriteLock orderLock = new ReentrantReadWriteLock();

    /**
     * There is a big boss and several shop assistants in the milk tea shop where a notebook is used to record orders.
     * The boss is responsible for writing a new order and the assistants are responsible for making products.
     * Only the boss can write this notebook, the assistants are just allowed to read it.
     * The notebook can be read by several assistants at the same time if only the book isn't writing new orders.
     **/
    public static void main(String[] args) {
        handleOrder();
    }

    public void addOrder() throws InterruptedException {
        orderLock.writeLock().lock();
        long waitingTime = (long) (Math.random() * 1000);
        Thread.sleep(waitingTime);
        System.out.println("Boss is adding a new order.");
        orderLock.writeLock().unlock();
    }

    public void viewOrder() throws InterruptedException {
        orderLock.readLock().lock();
        long readingTime = (long) (Math.random() * 500);
        Thread.sleep(readingTime);
        System.out.println(Thread.currentThread().getName() + ": is checking the order.");
        orderLock.readLock().unlock();
    }

    public static void handleOrder() {
        final HandleOrderExample orderExample = new HandleOrderExample();
        Thread boss = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        orderExample.addOrder();
                        long waitingTime = (long) (Math.random() * 1000);
                        Thread.sleep(waitingTime);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
        boss.start();

        int workerCount = 3;
        Thread[] workers = new Thread[workerCount];
        for (int i = 0; i < workerCount; i++) {
            workers[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            orderExample.viewOrder();
                            long workingTime = (long) (Math.random() * 5000);
                            Thread.sleep(workingTime);
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            });
            workers[i].start();
        }
    }
}
