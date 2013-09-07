package se.jkrau.sockettester.comm;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import se.jkrau.sockettester.SocketTester;
import se.jkrau.sockettester.packets.Handshake;
import se.jkrau.sockettester.packets.Packet;
import se.jkrau.sockettester.packets.Packets;
import se.jkrau.sockettester.packets.Ping;

public class Client extends Thread {
	
	private Socket clientSocket;
	private InetAddress targetAddress;
	private int targetPort;
	private String secretCode;
	
	public Client(InetAddress address, int port, String message) throws IOException {
		targetAddress = address;
		targetPort = port;
		secretCode = message;
	}
	
	public void run() {
		try {
			Handshake handshakePacket = new Handshake(secretCode);
			clientSocket = new Socket(targetAddress, targetPort);
			clientSocket.setTcpNoDelay(true);
			handshakePacket.write(new DataOutputStream(clientSocket.getOutputStream()));
			
			if (clientSocket.isClosed()) {
				System.out.println(SocketTester.getTimestamp() + "The server disconnected us.  Did you type in the correct secret code?");
				return;
			}
			
			// Ok it's ping testing time!
			
			System.out.println(SocketTester.getTimestamp() + "Successfully established ping testing! The console will report of any connection errors.  Now go get some coffee.");
			Thread.sleep(1000);
			
			Ping ping = new Ping();
			Packet receivedPacket;
			while (true) {
				ping.resetTimestamp();
				ping.write(new DataOutputStream(clientSocket.getOutputStream()));
				Thread.sleep(1000);
				
				receivedPacket = Packets.readStream(clientSocket.getInputStream());
				if (receivedPacket == null || !(receivedPacket instanceof Ping)) {
					System.out.println(SocketTester.getTimestamp() + "The server sent an invalid packet.");
					if (clientSocket.isClosed()) {
						System.out.println(SocketTester.getTimestamp() + "The server has disconnected us.");
						return;
					}
					continue;
				} else if (SocketTester.DEBUG_MODE) {
					Ping clientPing = (Ping) receivedPacket;
					System.out.println(SocketTester.getTimestamp() + "(DEBUG) Packet delay: " + ((System.currentTimeMillis() - clientPing.getTimestamp()) - 1000L) + "ms");
				}
				//Thread.sleep(1000);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.err.println(SocketTester.getTimestamp() + "Something went wrong with the server.");
	}
}
