package se.jkrau.sockettester.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import se.jkrau.sockettester.SocketTester;

public class Ping extends Packet {
	
	private long timestamp = 0L;
	public Ping() {
		resetTimestamp();
	}
	
	public void read(DataInputStream inStream) throws Exception {
		if (SocketTester.DEBUG_MODE) timestamp = inStream.readLong();
	}
	
	public void write(DataOutputStream outStream) throws Exception {
		outStream.writeByte(getPacketID());
		if (SocketTester.DEBUG_MODE) outStream.writeLong(timestamp);
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void resetTimestamp() {
		if (SocketTester.DEBUG_MODE) timestamp = System.currentTimeMillis();
	}
	
	public byte getPacketID() {
		return 0x2;
	}
}
