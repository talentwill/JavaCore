package core2.chapter06a.demo1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class UdpRecv {
    public static void main(String[] args) throws IOException, InterruptedException {
        DatagramSocket socket = new DatagramSocket(3000);
        byte[] buffer = new byte[1024];
        DatagramPacket packet1 = new DatagramPacket(buffer, buffer.length);

        System.out.println("UdpReceiver: waiting for new message.");

        socket.receive(packet1);
        String messageContent = new String(packet1.getData(), 0, packet1.getLength());
        String messageSenderAddress = new String(packet1.getAddress() + ":" + packet1.getPort());
        System.out.println("UdpReceiver: received a msg: " + messageContent + " from " + messageSenderAddress);

        Thread.sleep(100);

        String replyContent = "World";
        DatagramPacket packet2 = new DatagramPacket(replyContent.getBytes(), replyContent.length(),
                InetAddress.getByName("127.0.0.1"), packet1.getPort());
        socket.send(packet2);
        System.out.printf("UdpReceiver: replied: " + replyContent);
        socket.close();
    }
}
