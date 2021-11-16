package core2.chapter05b.demo9;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class MyTask implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("The time is " + new Date());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ScheduleExecutorTest {
    public static void main(String[] args) {
        executeAtFixedTime();
        executeFixedRate();
        executeFixedDelay();
    }

    public static void executeAtFixedTime() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(new MyTask(), 1, TimeUnit.SECONDS);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    public static void executeFixedRate() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        // Invoke task every 3s, if the previous task is still not finished when the timer is triggered,
        // invoke the next task immediately once the previous task is done.
        executorService.scheduleAtFixedRate(new MyTask(), 1, 3, TimeUnit.SECONDS);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    public static void executeFixedDelay() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        // Execute the task after the previous task is finished.
        executorService.scheduleWithFixedDelay(new MyTask(), 1, 3, TimeUnit.SECONDS);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}
