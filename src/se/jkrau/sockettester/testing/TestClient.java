package se.jkrau.sockettester.testing;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import se.jkrau.sockettester.packets.Handshake;
import se.jkrau.sockettester.packets.Packet;
import se.jkrau.sockettester.packets.Packets;

public class TestClient extends Thread {
	
	private Socket clientSocket;
	private InetAddress targetAddress;
	private int targetPort;
	
	public TestClient(InetAddress address, int port) throws IOException {
		targetAddress = address;
		targetPort = port;
	}
	
	public static void main(String[] args) {
		TestClient clientSocket;
		try {
			clientSocket = new TestClient(InetAddress.getByName("127.0.0.1"), 1080);
			clientSocket.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			Handshake handshakePacket = new Handshake("Testing!");
			clientSocket = new Socket(targetAddress, targetPort);
			handshakePacket.write(new DataOutputStream(clientSocket.getOutputStream()));
			System.out.println("Sent the handshake packet to the server successfully.");
			
			Packet receivedPacket = Packets.readStream(clientSocket.getInputStream());
			if (receivedPacket != null) {
				System.out.println("The server(" + clientSocket.getInetAddress().getHostAddress() + ") has sent the packet: " + receivedPacket.getPacketName());
			} else {
				System.out.println("No packet response from the server :(");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
