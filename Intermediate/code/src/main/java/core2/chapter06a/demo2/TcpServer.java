package core2.chapter06a.demo2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    public static void main(String[] args) {
        try {
            // tcp server socket
            ServerSocket serverSocket = new ServerSocket(8001);

            // blocked, waiting for client's connection
            Socket socket = serverSocket.accept();
            System.out.println("Welcome to the java world");

            // send message to client
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("Hello client.".getBytes());

            // receive client's message
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            System.out.println("Client said: " + bufferedReader.readLine());

            serverSocket.close();
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
