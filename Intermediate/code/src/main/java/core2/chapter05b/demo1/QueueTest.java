package core2.chapter05b.demo1;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;

class QueueThread implements Runnable {
    public Queue<String> queue;

    QueueThread(Queue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            queue.add(Thread.currentThread().getName() + i);
        }
    }
}

class QueueTest {
    public static void main(String[] args) {
        try {
            test();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test() throws InterruptedException {
        Queue<String> unsafeQueue = new ArrayDeque<>();
        ConcurrentLinkedDeque<String> safeQueue1 = new ConcurrentLinkedDeque<>();
        ArrayBlockingQueue<String> safeQueue2 = new ArrayBlockingQueue<>(100);

        QueueThread t1 = new QueueThread(unsafeQueue);
        QueueThread t2 = new QueueThread(safeQueue1);
        QueueThread t3 = new QueueThread(safeQueue2);

        for (int i = 0; i < 10; i++) {
            new Thread(t1, String.valueOf(i)).start();
            new Thread(t2, String.valueOf(i)).start();
            new Thread(t3, String.valueOf(i)).start();
        }

        Thread.sleep(2000);

        System.out.println("QueueThread1.queue.size = " + t1.queue.size());
        System.out.println("QueueThread2.queue.size = " + t2.queue.size());
        System.out.println("QueueThread3.queue.size = " + t3.queue.size());
    }
}
