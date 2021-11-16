package core2.chapter05b.demo7;

import java.util.Scanner;
import java.util.concurrent.Exchanger;

class BackgroundWorker implements Runnable {
    final Exchanger<String> exchanger;

    BackgroundWorker(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String item = exchanger.exchange(null); // Blocked, wait for exchange, get the user's input
                switch (item) {
                    case "zhangsan":
                        exchanger.exchange("50");
                        break;
                    case "lisi":
                        exchanger.exchange("60");
                        break;
                    case "wangwu":
                        exchanger.exchange("70");
                        break;
                    case "exit":
                        exchanger.exchange("exit");
                        return;
                    default:
                        exchanger.exchange("No such student found");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class ExchangerExample {
    /**
     * This example implements student score query by using Exchanger, 
     * which shows how simple data exchange between threads.
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Exchanger<String> exchanger = new Exchanger<>();
        BackgroundWorker worker = new BackgroundWorker(exchanger);
        new Thread(worker).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please enter the name of the student you want to query: ");
            String input = scanner.nextLine().trim();
            exchanger.exchange(input); // Pass the input to background thread.
            String value = exchanger.exchange(null); // wait for background's exchange.
            if (value.equals("exit"))
                break; 
            System.out.println("The query result is " + value);
        }
        scanner.close();
    }
}