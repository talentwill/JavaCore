package core2.chapter06b.homework;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NioChatRoomServer {
    int port = 8001;
    Selector selector;
    ServerSocketChannel serverSocketChannel;
    List<SocketChannel> clients = new ArrayList<>();

    public NioChatRoomServer() {
    }

    public void start() {
        try {
            listen();
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listen() throws IOException {
        try {
            // initialize selector
            selector = Selector.open();

            // initialize server channel
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));

            // register accept event
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server is listening on port 8001.");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void run() {
        while (true) {
            try {
                selector.select(1000);
                // traverse select events
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                        key.cancel();
                        key.channel().close();
                        clients.remove(key.channel());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                handleAcceptEvent(key);
            }
            if (key.isReadable()) {
                handleReadEvent(key);
            }
        }
    }

    private void handleAcceptEvent(SelectionKey key) throws IOException {
        // create new connection socket via accept.
        SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
        channel.configureBlocking(false); // set none-block channel
        channel.register(selector, SelectionKey.OP_READ); // register read event
        clients.add(channel);
    }

    private void handleReadEvent(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        // initialize read buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // read bytes from channel
        int size = channel.read(buffer);
        // handle input bytes
        if (size > 0) { // read success
            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            String content = new String(bytes, StandardCharsets.UTF_8);

            // 1. print client's message
            System.out.println("Server received: " + content);

            // 2. forward message to others
            for (SocketChannel client : clients) {
                if (client != channel) {
                    buffer.flip();
                    client.write(buffer);
                }
            }
        } else if (size < 0) { // channel closed
            key.cancel();
            channel.close();
            clients.remove(channel);
        } else {
            ; // do nothing
        }
    }

    public static void main(String[] args) {
        new NioChatRoomServer().start();
    }
}
