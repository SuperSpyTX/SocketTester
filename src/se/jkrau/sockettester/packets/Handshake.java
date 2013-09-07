package se.jkrau.sockettester.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Handshake extends Packet {
	
	private String message;
	
	public Handshake() {
		message = null;
	}
	
	public Handshake(String message) {
		this.message = message;
	}
	
	public void read(DataInputStream inStream) throws Exception {
		message = inStream.readUTF().trim();
	}
	
	public void write(DataOutputStream outStream) throws Exception {
		outStream.writeByte(getPacketID());
		outStream.writeUTF(message);
	}
	
	public String getMessage() {
		return message;
	}
	
	public byte getPacketID() {
		return 0x1;
	}
}
