package core2.chapter05b.demo1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class ListThread implements Runnable {
    public List<String> list;

    public ListThread(List<String> list) {
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.add(Thread.currentThread().getName() + i);
        }
    }
}

public class ListTest {
    public static void main(String[] args) {
        try {
            test();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test() throws InterruptedException {
        List<String> unsafeList = new ArrayList<>();
        List<String> safeList1 = Collections.synchronizedList(new ArrayList<String>());
        CopyOnWriteArrayList<String> safeList2 = new CopyOnWriteArrayList<>();

        ListThread t1 = new ListThread(unsafeList);
        ListThread t2 = new ListThread(safeList1);
        ListThread t3 = new ListThread(safeList2);

        for (int i = 0; i < 10; i++) {
            new Thread(t1, String.valueOf(i)).start();
            new Thread(t2, String.valueOf(i)).start();
            new Thread(t3, String.valueOf(i)).start();
        }

        Thread.sleep(2000);
        System.out.println("ListThread1.list.size() = " + t1.list.size());
        System.out.println("ListThread2.list.size() = " + t2.list.size());
        System.out.println("ListThread3.list.size() = " + t2.list.size());
    }
}