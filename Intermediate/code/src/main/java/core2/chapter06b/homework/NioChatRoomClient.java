package core2.chapter06b.homework;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NioChatRoomClient {
    private int port = 8001;
    private Selector selector;
    private SocketChannel channel;
    ScheduledExecutorService executorService;
    String clientId;

    public NioChatRoomClient() {
        // create client Id
        clientId = UUID.randomUUID().toString().substring(0, 6);
        // chat task timer
        executorService = Executors.newScheduledThreadPool(1);
    }

    public void start() throws IOException {
        connect();
        run();
    }

    private void connect() {
        try {
            selector = Selector.open();
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            if (channel.connect(new InetSocketAddress("localhost", port))) {
                channel.register(selector, SelectionKey.OP_READ);
                startChatTask();
            } else {
                channel.register(selector, SelectionKey.OP_CONNECT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() throws IOException {
        while (true) {
            selector.select(1000);
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();
                try {
                    handleInput(key);
                } catch (IOException e) {
                    e.printStackTrace();
                    key.cancel();
                    key.channel().close();
                    executorService.shutdown();
                }
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isConnectable()) {
                handleConnectEvent(key);
            }
            if (key.isReadable()) {
                handleReadEvent(key);
            }
        }
    }

    private void handleConnectEvent(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        if (channel.finishConnect()) {
            channel.register(selector, SelectionKey.OP_READ);
            startChatTask();
        }
    }

    private void handleReadEvent(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int size = channel.read(buffer);
        if (size > 0) {
            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            String content = new String(bytes, StandardCharsets.UTF_8);
            System.out.println(content);
        } else if (size < 0) {
            key.cancel();
            channel.close();
        } else {
            ;
        }
    }

    private void startChatTask() {
        System.out.println("Client:" + clientId + " entered the chat room.");
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                writeMessage();
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

    private void writeMessage() {
        // message format: "dfe0fd: HH:MM:SS"
        byte[] message = (clientId + ": " + new Date().toString().substring(11, 19)).getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(message.length);
        buffer.put(message);
        buffer.flip();

        try {
            if (channel.isConnected()) {
                channel.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new NioChatRoomClient().start();
    }
}
