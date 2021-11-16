package core2.chapter06b.demo2;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class AioServer {
    public static void main(String[] args) throws Exception {
        final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();

        // 1 bind to localhost
        server.bind(new InetSocketAddress("localhost", 8001));
        System.out.println("Server is listening on port 8001");

        // 2 wait for new connection
        server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(final AsynchronousSocketChannel channel, Object attachment) {
                server.accept(null, this); // continue to accept other request

                // 2.1 read data from client
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                channel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result_num, ByteBuffer attachment) {
                        // copy data from byte buffer to char buffer
                        // convert the byte stream to human charset(utf-8)
                        attachment.flip(); // prepare the buffer
                        CharBuffer charBuffer = CharBuffer.allocate(1024);
                        CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
                        decoder.decode(attachment, charBuffer, false);

                        // copy char buffer to string.
                        charBuffer.flip();
                        String data = new String(charBuffer.array(), 0, charBuffer.limit());

                        // handle request from client.
                        System.out.println("Client said: " + data);

                        // reply to client
                        channel.write(ByteBuffer.wrap((data + ", 666").getBytes()));
                        try {
                            channel.close(); // close the connection of the client.
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        System.out.println("read error " + exc.getMessage());
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("failed: " + exc.getMessage());
            }
        });

        // 3 make server always running.
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
