package core2.chapter05b.demo4;

import java.util.concurrent.CountDownLatch;

class CountDownLatchExample {
    /**
     * 100M race, players start to run after the starting gun blast, 
     * the race is over after all players run to the finish line.
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        int workerCount = 10;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(workerCount);
        Thread[] workers = new Thread[workerCount];
        for (int i = 0; i < workerCount; i++) {
            workers[i] = new Thread(new Worker(startSignal, doneSignal));
            workers[i].start();
        }

        System.out.println("Preparation ...");
        System.out.println("Ready");
        startSignal.countDown();
        System.out.println("Race start！");
        doneSignal.await();
        System.out.println("Race end！");
    }

    static class Worker implements Runnable {
        CountDownLatch startSignal;
        CountDownLatch doneSignal;

        Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run() {
            try {
                startSignal.await(); // wait for the start gun
                doWork();
                doneSignal.countDown();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        private void doWork() throws InterruptedException {
            System.out.println(Thread.currentThread().getName() + ": Running!");
            long runningTime = (long) (Math.random() * 2000);
            Thread.sleep(runningTime);
            System.out.println(Thread.currentThread().getName() + ": Running over!");
        }
    }
}
