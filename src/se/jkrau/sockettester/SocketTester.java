package se.jkrau.sockettester;

import java.net.Inet4Address;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import se.jkrau.sockettester.comm.Client;
import se.jkrau.sockettester.comm.Server;

public class SocketTester {
	
	public static boolean DEBUG_MODE = false;
	
	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println(getTimestamp() + "USAGE: SocketTester.jar <client/server> <server ip/bind ip> <server port/connect port> <secret code> (debug)");
			return;
		}
		
		try {
			boolean server = args[0].equals("server");
			if (args.length > 4) {
				DEBUG_MODE = args[4].equals("debug");
			}
			String ip = args[1];
			int port = Integer.parseInt(args[2]);
			String secretcode = args[3];
			if (server) {
				Server socket = (ip.equals("0.0.0.0") || ip.equals("127.0.0.1") ? new Server(port, secretcode) : new Server(Inet4Address.getByName(ip), port, secretcode));
				socket.start();
				System.out.println(getTimestamp() + "Server has been started successfully.");
			} else {
				Client socket = (new Client(Inet4Address.getByName(ip), port, secretcode));
				socket.start();
				System.out.println(getTimestamp() + "Client has been started successfully.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(getTimestamp() + "Something went wrong.  Maybe invalid parameters?");
		}
	}
	
	public static String getTimestamp() {
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a zzz");
		
		return "[" + ft.format(dNow) + "] ";
	}
	
}
