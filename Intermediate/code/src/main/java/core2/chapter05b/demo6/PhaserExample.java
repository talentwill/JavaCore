package core2.chapter05b.demo6;

import java.util.concurrent.Phaser;

class Student implements Runnable {
    final Phaser phaser;

    Student(Phaser phaser) {
        this.phaser = phaser;
    }

    private void doTesting(int quizId) throws InterruptedException {
        String name = Thread.currentThread().getName();
        System.out.println(name + "Start to answer question no." + quizId);
        long thinkingTime = (long) (Math.random() * 1000);
        Thread.sleep(thinkingTime);
        System.out.println(name + "Question no." + quizId + " answered.");
    }

    @Override
    public void run() {
        try {
            doTesting(1);
            phaser.arriveAndAwaitAdvance();
            doTesting(2);
            phaser.arriveAndAwaitAdvance();
            doTesting(3);
            phaser.arriveAndAwaitAdvance();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class PhaserExample {
    /**
     * Suppose we have an exam with three big questions, one at a time,
     * and then process to next one after all the students finish it.
     * @param args
     */
    public static void main(String[] args) {
        int studentCount = 5;
        Phaser phaser = new Phaser(studentCount);
        for (int i = 0; i < studentCount; i++) {
            new Thread(new Student(phaser)).start();
        }
    }
}