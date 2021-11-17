package core2.chapter06a.demo2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Worker implements Runnable {
    Socket socket;

    Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            while (true) {
                // blocked, waiting for client's input
                String word = bufferedReader.readLine();
                System.out.println("client said:" + word + ":" + word.length());

                if (word.equalsIgnoreCase("quit")) {
                    System.out.println("Worker quits.");
                    break;
                }

                // send echo to client
                String echo = word + " 666";
                System.out.println("server said:" + word + "---->" + echo);
                // dataOutputStream.writeBytes(strWord +"---->"+ strEcho +"\r\n");
                dataOutputStream.writeBytes(word + "---->" + echo + System.getProperty("line.separator"));
            }

            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class TcpServer2 {
    public static void main(String[] args) {
        try {
            // tcp socket
            ServerSocket serverSocket = new ServerSocket(8001);
            while (true) {
                // blocked, waiting for client connection.
                Socket socket = serverSocket.accept();
                System.out.println("Welcome to the java world.");

                // create thread to handle client's request
                new Thread(new Worker(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
