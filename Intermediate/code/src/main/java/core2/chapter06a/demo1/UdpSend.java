package core2.chapter06a.demo1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class UdpSend {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        String hello = "Hello";
        DatagramPacket packet = new DatagramPacket(hello.getBytes(), hello.length(),
                InetAddress.getByName("127.0.0.1"), 3000);
        socket.send(packet);
        System.out.println("UdpSender: sent a msg: " + hello);

        Thread.sleep(1000);

        byte[] buffer = new byte[1024];
        DatagramPacket packet1 = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet1);
        String messageContent = new String(packet1.getData(), 0, packet1.getLength());
        String messageSenderAddress = new String(packet1.getAddress() + ":" + packet1.getPort());
        System.out.println("UdpSender: received a msg: " + messageContent + " from " + messageSenderAddress);
    }
}
