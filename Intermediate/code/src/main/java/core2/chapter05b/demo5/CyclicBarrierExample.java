package core2.chapter05b.demo5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class CalculateEachRow implements Runnable {
    final int[][] numbers;
    final CyclicBarrier barrier;
    final int rowNumber;
    final int[] result;

    CalculateEachRow(CyclicBarrier barrier, int[][] numbers, int rowNumber, int[] result) {
        this.barrier = barrier;
        this.numbers = numbers;
        this.rowNumber = rowNumber;
        this.result = result;
    }

    @Override
    public void run() {
        int[] row = numbers[rowNumber];
        for (int i = 0; i < row.length; i++) {
            result[rowNumber] += row[i];
        }
        try {
            Thread.sleep((long) (Math.random() * 1000));
            System.out.println(Thread.currentThread().getName() + ": Calculated the no." + (rowNumber + 1) + "row，result is: " + result[rowNumber]);
            barrier.await(); // Wait until the barrier await is up to 3
            System.out.println(Thread.currentThread().getName() + ": Calculation end.");
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

class CalculateFinalResult implements Runnable {
    final int[] eachRowResult;
    int finalResult;

    CalculateFinalResult(int[] eachRowResult) {
        this.eachRowResult = eachRowResult;
    }

    public int getFinalResult() {
        return finalResult;
    }

    @Override
    public void run() {
        for (int i = 0; i < eachRowResult.length; i++) {
            finalResult += eachRowResult[i];
        }
        System.out.println("The final result is: " + finalResult);
    }
}

public class CyclicBarrierExample {
    public static void main(String[] args) {
        final int rowCount = 3;
        final int colCount = 5;

        int[] eachResult = new int[rowCount];
        int[][] numbers = new int[rowCount][colCount];
        int[] row1 = new int[]{1, 2, 3, 4, 5};
        int[] row2 = new int[]{6, 7, 8, 9, 10};
        int[] row3 = new int[]{11, 12, 13, 14, 15};
        numbers[0] = row1;
        numbers[1] = row2;
        numbers[2] = row3;

        CalculateFinalResult finalResultCalculator = new CalculateFinalResult(eachResult);
        CyclicBarrier barrier = new CyclicBarrier(3, finalResultCalculator); // await is done, invoke callback.
        for (int i = 0; i < rowCount; i++) {
            new Thread(new CalculateEachRow(barrier, numbers, i, eachResult)).start();
        }
    }
}
