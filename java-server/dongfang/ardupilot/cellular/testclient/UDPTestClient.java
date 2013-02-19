package dongfang.ardupilot.cellular.testclient;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPTestClient extends Thread {
    DatagramSocket socket;
    InetAddress address;
    int port;

    UDPTestClient(String hostname, int remotePort) throws IOException {
	address = InetAddress.getByName(hostname);
	socket = new DatagramSocket();
	this.port = remotePort;
    }

    public void run() {
	while(true) {
	    try {
		byte[] foo = "Hi there!".getBytes();
		byte[] buf = new byte[256];
		while(true) {
		    DatagramPacket packet = new DatagramPacket(foo, foo.length, address, port);		    
		    socket.send(packet);
		    System.out.println("Sent from " + socket.getLocalSocketAddress() + " to " + packet.getSocketAddress());

		    packet = new DatagramPacket(buf, buf.length);
		    socket.receive(packet);
		    String received = new String(packet.getData(), 0, packet.getLength());
		    System.out.println("Received: " + received);
		}
	    } catch (IOException ex) {
		System.err.println(ex);
	    }
	}
    }

    public static void main(String[] args) throws Exception {
	int UDPPort = Integer.parseInt(args[0]);
	new UDPTestClient("localhost", UDPPort).start();
    }
}