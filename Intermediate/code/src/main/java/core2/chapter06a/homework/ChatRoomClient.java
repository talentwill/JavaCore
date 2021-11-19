package core2.chapter06a.homework;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

class ClientSender implements Runnable {
    private String name = new String();
    private DataOutputStream dataOutputStream;

    ClientSender(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
        this.name = "People-" + (char) ('A' + Math.random() * 1000 % 26);
        System.out.println(name + " came!");
    }

    private String sayOneSentence(){
        return name + " said: " + new Date().toString().substring(11, 19);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
                dataOutputStream.writeBytes(sayOneSentence() + System.getProperty("line.separator"));
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

class ClientReceiver implements Runnable {
    private BufferedReader reader;

    public ClientReceiver(BufferedReader bufferedReader) {
        this.reader = bufferedReader;
    }

    private void displayMessage(String data) {
        System.out.println("Received: " + data);
    }

    @Override
    public void run() {
        while (true) {
            try {
                String data = reader.readLine();
                displayMessage(data);
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

public class ChatRoomClient {
    private static String ip = "127.0.0.1";
    private static int port = 9000;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(InetAddress.getByName(ip), port);
            InputStream ips = socket.getInputStream();
            OutputStream ops = socket.getOutputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ips));
            DataOutputStream dataOutputStream = new DataOutputStream(ops);

            Thread receiver = new Thread(new ClientReceiver(bufferedReader));
            Thread sender = new Thread(new ClientSender(dataOutputStream));
            receiver.start();
            sender.start();
            receiver.join();
            sender.join();

            ips.close();
            dataOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
