package se.jkrau.sockettester.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet {
	
	public void read(DataInputStream inStream) throws Exception {
		throw new Exception("Packet read(inStream) has not been implemented!");
	}
	
	public void write(DataOutputStream outStream) throws Exception {
		throw new Exception("Packet write(inStream) has not been implemented!");
	}
	
	public String getPacketName() {
		return this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1);
	}
	
	public byte getPacketID() {
		throw new IllegalStateException("Packet getPacketID() has not been implemented!");
	}
}
