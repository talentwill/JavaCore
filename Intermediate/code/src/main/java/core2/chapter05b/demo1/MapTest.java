package core2.chapter05b.demo1;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class MapThread implements Runnable {
    public Map<Integer, String> map;

    public MapThread(Map<Integer, String> map) {
        this.map = map;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.put(i, Thread.currentThread().getName() + i);
        }
    }
}

public class MapTest {
    public static void main(String[] args) {
        try {
            test();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test() throws InterruptedException {
        Map<Integer, String> unsafeMap = new HashMap<>();
        Map<Integer, String> safeMap1 = Collections.synchronizedMap(new HashMap<Integer, String>());
        ConcurrentHashMap<Integer, String> safeMap2 = new ConcurrentHashMap<>();
        MapThread t1 = new MapThread(unsafeMap);
        MapThread t2 = new MapThread(safeMap1);
        MapThread t3 = new MapThread(safeMap2);

        for (int i = 0; i < 10; i++) {
            new Thread(t1, String.valueOf(i)).start();
            new Thread(t2, String.valueOf(i)).start();
            new Thread(t3, String.valueOf(i)).start();
        }

        Thread.sleep(2000);

        System.out.println("MapThread1.map.size = " + t1.map.size());
        System.out.println("MapThread2.map.size = " + t2.map.size());
        System.out.println("MapThread3.map.size = " + t3.map.size());
    }
}
