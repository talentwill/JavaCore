package core2.chapter05b.demo3;

import java.util.concurrent.Semaphore;

class SemaphoreExample {
    private final Semaphore placeSemaphore = new Semaphore(5);

    public boolean parking() {
        if (placeSemaphore.tryAcquire()) {
            System.out.println(Thread.currentThread().getName() + ": Parking succeed.");
            return true;
        } else {
            System.out.println(Thread.currentThread().getName() + ": No place to park.");
            return false;
        }
    }

    public void leaving() {
        placeSemaphore.release();
        System.out.println(Thread.currentThread().getName() + ": Left.");
    }

    public static void main(String[] args) throws InterruptedException {
        final SemaphoreExample semaphoreExample = new SemaphoreExample();

        int tryToParkCount = 10;
        Thread[] parkers = new Thread[tryToParkCount];
        for (int i = 0; i < tryToParkCount; i++) {
            parkers[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            long randomTime = (long) (Math.random() * 1000);
                            Thread.sleep(randomTime);
                            if (semaphoreExample.parking()) {
                                long parkingTime = (long) (Math.random() * 1200);
                                Thread.sleep(parkingTime);
                                semaphoreExample.leaving();
                                return;
                            }
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            });
            parkers[i].start();
        }

        for (int i = 0; i < tryToParkCount; i++) {
            parkers[i].join();
        }
    }
}
