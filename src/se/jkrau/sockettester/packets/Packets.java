package se.jkrau.sockettester.packets;

import java.io.DataInputStream;
import java.io.InputStream;

public enum Packets {
	
	HANDSHAKE(new Handshake()), PING(new Ping());
	
	Packet packet = null;
	byte packetID;
	
	Packets(Packet packet) {
		this.packet = packet;
		this.packetID = packet.getPacketID();
	}
	
	public static Packet readStream(InputStream inStream) throws Exception {
		DataInputStream readStream = new DataInputStream(inStream);
		byte packetID = readStream.readByte();
		Packet packet = null;
		
		for (Packets entry : Packets.values()) {
			if (entry.packetID == packetID) {
				packet = entry.packet.getClass().newInstance();
			}
		}
		
		if (packet == null) return null;
		
		packet.read(readStream);
		
		return packet;
	}
	
	public static Packet getPacketByID(byte packetID) throws Exception {
		for (Packets entry : Packets.values()) {
			if (entry.packetID == packetID) { return (Packet) entry.packet.getClass().newInstance(); }
		}
		
		return null;
	}
}
