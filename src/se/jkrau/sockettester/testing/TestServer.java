package se.jkrau.sockettester.testing;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import se.jkrau.sockettester.packets.Handshake;
import se.jkrau.sockettester.packets.Packet;
import se.jkrau.sockettester.packets.Packets;

public class TestServer extends Thread {
	
	private ServerSocket serverSocket;
	
	public TestServer(InetAddress address, int port) throws IOException {
		serverSocket = new ServerSocket(port, Integer.MAX_VALUE, address);
	}
	
	public TestServer(int port) throws IOException {
		serverSocket = new ServerSocket(port, Integer.MAX_VALUE);
	}
	
	public static void main(String[] args) {
		TestServer serverSocket;
		try {
			serverSocket = new TestServer(1080);
			serverSocket.start();
			System.out.println("Server has been started successfully!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			try {
				Socket client = serverSocket.accept();
				Packet readPacket = Packets.readStream(client.getInputStream());
				if (readPacket == null) {
					System.out.println("The client has sent an invalid packet, disconnecting client.");
					client.close();
					continue;
				}
				
				System.out.println("The client(" + client.getInetAddress().getHostAddress() + ") has sent the packet: " + readPacket.getPacketName());
				
				Handshake handshakePacket = new Handshake("Hello!");
				handshakePacket.write(new DataOutputStream(client.getOutputStream()));
				
				System.out.println("Sent a handshake back to the client.");
				
				//client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
