package core2.chapter06b.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.UUID;

public class AioClient {
    public static void main(String[] args) throws IOException {
        try {
            final AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();

            channel.connect(new InetSocketAddress("localhost", 8001), null, new CompletionHandler<Void, Void>() {
                @Override
                public void completed(Void result, Void attachment) {
                    final String request = UUID.randomUUID().toString();

                    // 1 send request to server
                    channel.write(ByteBuffer.wrap(request.getBytes()), null, new CompletionHandler<Integer, Object>() {
                        @Override
                        public void completed(Integer result, Object attachment) {
                            System.out.println("Write " + request + ", and wait for response.");

                            // 2 receive response from server
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            channel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                                @Override
                                public void completed(Integer result, ByteBuffer attachment) {
                                    // 2.1 decode response data
                                    attachment.flip();
                                    CharBuffer charBuffer = CharBuffer.allocate(1024);
                                    CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
                                    // decode byte stream to characters
                                    decoder.decode(attachment, charBuffer, false);
                                    charBuffer.flip();

                                    // 2.2 handle response
                                    String response = new String(charBuffer.array(), 0, charBuffer.limit());
                                    System.out.println("Server said: " + response);

                                    // 2.3 close channel to server
                                    try {
                                        channel.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment) {
                                    System.out.println("Read error " + exc.getMessage());
                                }
                            });
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            System.out.println("Failed " + exc.getMessage());
                        }
                    });
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    System.out.println("Failed " + exc.getMessage());
                }
            });

            Thread.sleep(5000); // wait a moment for server response.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
