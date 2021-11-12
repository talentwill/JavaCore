package core2.chapter05b.demo2;

import java.util.concurrent.locks.ReentrantLock;

public class BuyMilkTeaExample
{
    private static final ReentrantLock queueLock = new ReentrantLock();

    /**
     * There is a milk tea shop and customers need to line up to buy milk tea
     * The customer will be replied 'wait a moment'
     * when she asks for a milk tea if the line isn't empty.
     **/
    public static void main(String[] args) throws InterruptedException {
        buyMilkTea();
    }

    public void tryBuyMilkTea() throws InterruptedException {
        boolean flag = true;
        while (flag) {
            if (queueLock.tryLock()) {
//                queueLock.lock(); // no return, thread is blocked and wait
                long thinkTime = (long) (Math.random() * 500);
                Thread.sleep(thinkTime);
                System.out.println(Thread.currentThread().getName() + "： I'd like a milk tea without sugar!");
                flag = false;
                queueLock.unlock();
            } else {
//                System.out.println(Thread.currentThread().getName() + "：" + queueLock.getQueueLength() + "people are waiting");
                System.out.println(Thread.currentThread().getName() + ": Wait a moment!");
            }
            if (flag) {
                Thread.sleep(1000);
            }
        }
    }

    public static void buyMilkTea() throws InterruptedException {
        final BuyMilkTeaExample lockExample = new BuyMilkTeaExample();
        int STUDENT_COUNT = 10;
        Thread[] students = new Thread[STUDENT_COUNT];
        for (int i = 0; i < STUDENT_COUNT; i++) {
            students[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        long walkTime = (long) (Math.random() * 1000);
                        Thread.sleep(walkTime);
                        lockExample.tryBuyMilkTea();
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
            students[i].start();
        }

        for (int i = 0; i < STUDENT_COUNT; i++) {
            students[i].join();
        }
    }
}
