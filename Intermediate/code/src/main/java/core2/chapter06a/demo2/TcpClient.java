package core2.chapter06a.demo2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {
    public static void main(String[] args) {
        try {
            // tcp socket
            Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 8001);

            // input channel
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // output channel
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader bufferedReaderFromKeyboard = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                // Receive user keyboard input from console
                System.out.println("Please enter your message:");
                String input = bufferedReaderFromKeyboard.readLine();

                // sent quit
                if (input.equalsIgnoreCase("quit")) {
                    dataOutputStream.writeBytes(input + System.getProperty("line.separator"));
                    break;
                }

                // send message to server
                System.out.println("I want to send:" + input);
                dataOutputStream.writeBytes(input + System.getProperty("line.separator"));

                // receive server's response
                System.out.println("Server said: " + bufferedReader.readLine());
            }
            dataOutputStream.close();
            bufferedReaderFromKeyboard.close();
            bufferedReader.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
