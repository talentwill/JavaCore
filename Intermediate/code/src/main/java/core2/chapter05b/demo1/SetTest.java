package core2.chapter05b.demo1;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class SetThread implements Runnable {
    public Set<String> set;

    public SetThread(Set<String> set) {
        this.set = set;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            set.add(Thread.currentThread().getName() + i);
        }
    }
}

public class SetTest {
    public static void main(String[] args) {
        try {
            test();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test() throws InterruptedException {
        Set<String> unsafeSet = new HashSet<>();
        Set<String> safeSet1 = Collections.synchronizedSet(new HashSet<String>());
        CopyOnWriteArraySet<String> safeSet2 = new CopyOnWriteArraySet<>();

        SetThread t1 = new SetThread(unsafeSet);
        SetThread t2 = new SetThread(safeSet1);
        SetThread t3 = new SetThread(safeSet2);

        for (int i = 0; i < 10; i++) {
            new Thread(t1, String.valueOf(i)).start();
            new Thread(t2, String.valueOf(i)).start();
            new Thread(t3, String.valueOf(i)).start();
        }

        Thread.sleep(2000);
        System.out.println("SetThread1.set.size = " + t1.set.size());
        System.out.println("SetThread2.set.size = " + t2.set.size());
        System.out.println("SetThread3.set.size = " + t3.set.size());
    }
}
