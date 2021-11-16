package core2.chapter05b.demo8;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

class MyTimerTask1 extends TimerTask {
    @Override
    public void run() {
        System.out.println("Task1: Ran！Time is " + new Date());
    }
}

class MyTimerTask2 extends TimerTask {
    @Override
    public void run() {
        System.out.println("Task2: Time is " + new Date());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class TimerTest {
    public static void main(String[] args) throws InterruptedException {
        MyTimerTask1 task1 = new MyTimerTask1();
        Timer timer = new Timer();
        timer.schedule(task1, 1000, 1000); // Delay 1s and execute every 2 seconds.
        System.out.println("Current time is " + new Date());
        Thread.sleep(4000);
        task1.cancel(); // it must be cancelled, or it will be invoked repeatedly

        System.out.println("================================");

        Calendar now = Calendar.getInstance();
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) + 5);
        Date runTime = now.getTime();
        timer.scheduleAtFixedRate(new MyTimerTask2(), runTime, 5000);

        Thread.sleep(20000);
        timer.cancel();
    }
}
