package se.jkrau.sockettester.comm;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import se.jkrau.sockettester.SocketTester;
import se.jkrau.sockettester.packets.Handshake;
import se.jkrau.sockettester.packets.Packet;
import se.jkrau.sockettester.packets.Packets;
import se.jkrau.sockettester.packets.Ping;

public class Server extends Thread {
	
	private ServerSocket serverSocket;
	private String secretCode;
	
	public Server(InetAddress address, int port, String message) throws IOException {
		serverSocket = new ServerSocket(port, Integer.MAX_VALUE, address);
		secretCode = message;
	}
	
	public Server(int port, String message) throws IOException {
		serverSocket = new ServerSocket(port, Integer.MAX_VALUE);
		secretCode = message;
	}
	
	public void run() {
		while (true) {
			try {
				Socket client = null;
				try {
					client = serverSocket.accept();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					continue;
				}
				client.setTcpNoDelay(true);
				Packet readPacket = Packets.readStream(client.getInputStream());
				if (readPacket == null || !(readPacket instanceof Handshake)) {
					System.out.println(SocketTester.getTimestamp() + "The client has sent an invalid packet, disconnecting client.");
					client.close();
					continue;
				}
				
				// Look for correct secret message.
				if (!((Handshake) readPacket).getMessage().contains(secretCode)) {
					System.out.println(SocketTester.getTimestamp() + "The client has sent a bad secret message(" + ((Handshake) readPacket).getMessage() + "), disconnecting client.");
					client.close();
					continue;
				}
				
				System.out.println(SocketTester.getTimestamp() + "Successfully established ping testing! The console will report of any connection errors.  Now go get some coffee.");
				Thread.sleep(1000);
				Ping ping = new Ping();
				Packet receivedPacket;
				while (true) {
					ping.resetTimestamp();
					ping.write(new DataOutputStream(client.getOutputStream()));
					
					receivedPacket = Packets.readStream(client.getInputStream());
					if (receivedPacket == null || !(receivedPacket instanceof Ping)) {
						System.err.println(SocketTester.getTimestamp() + "The client sent an invalid packet.");
						if (serverSocket.isClosed()) {
							System.err.println(SocketTester.getTimestamp() + "The client disconnected.");
							break;
						}
						continue;
					} else if (SocketTester.DEBUG_MODE) {
						Ping clientPing = (Ping) receivedPacket;
						System.out.println(SocketTester.getTimestamp() + "(DEBUG) Packet delay: " + ((System.currentTimeMillis() - clientPing.getTimestamp())) + "ms");
					}
					Thread.sleep(1000);
				}
				//client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.err.println(SocketTester.getTimestamp() + "Something went wrong with the client.");
		}
	}
}
