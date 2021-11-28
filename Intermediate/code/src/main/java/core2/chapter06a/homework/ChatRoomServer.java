package core2.chapter06a.homework;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Client implements Runnable {
    public final int id;
    private Socket socket;
    private MessageForwarder forwarder;

    public Client(Socket socket, int id, MessageForwarder forwarder) {
        this.socket = socket;
        this.id = id;
        this.forwarder = forwarder;
    }

    private void displayMessage(String data) {
        System.out.printf("Received [%s] from client-%d.\n", data, id);
    }

    public void sendMessage(String data) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeBytes(data + System.getProperty("line.separator"));
            System.out.printf("Sent [%s] to client-%d\n", data, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            InputStream ips = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ips));

            while (true) {
                String data = bufferedReader.readLine();
                displayMessage(data);
                forwarder.forwardMessage(this, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class MessageForwarder {
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private List<Client> clientList = new ArrayList<>();

    public void addClient(Client client) {
        readWriteLock.writeLock().lock();
        clientList.add(client);
        System.out.printf("New client id=%d added\n", client.id);
        readWriteLock.writeLock().unlock();
    }

    public void forwardMessage(Client sender, String message) {
        readWriteLock.readLock().lock();
        for (int i = 0; i < clientList.size(); i++) {
            if (clientList.get(i).id != sender.id) {
                clientList.get(i).sendMessage(message);
            }
        }
        readWriteLock.readLock().unlock();
    }
}

public class ChatRoomServer {
    public static void main(String[] args) throws IOException {
        int port = 8001;
        ServerSocket serverSocket = null;
        try {
            int clientId = 1;
            MessageForwarder messageForwarder = new MessageForwarder();

            // initialize server socket
            serverSocket = new ServerSocket(port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                // start a dedicated thread
                Client client = new Client(clientSocket, clientId++, messageForwarder);
                messageForwarder.addClient(client);
                new Thread(client).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}
