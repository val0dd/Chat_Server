package me.valodd.chatserver.network.packet.boths;

import me.valodd.chatserver.network.BufferConnection;
import me.valodd.chatserver.network.Packet;
import me.valodd.chatserver.network.packet.PACKETS;

public class PacketConnection extends Packet {
	private String username;
	private int nbClients;
	private String clients;

	public PacketConnection() {
		super();
	}

	@Override
	public void readPacket(BufferConnection bc) {
		username = bc.readString();
	}

	@Override
	public void writePacket(BufferConnection bc) {
		bc.writeString(username);
		bc.writeInt(nbClients);
		bc.writeString(clients);
	}

	@Override
	public PACKETS getPacketID() {
		return PACKETS.PACKETCONNECTION;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setNbClients(int nbClients) {
		this.nbClients = nbClients;
	}

	public void setClients(String clients) {
		this.clients = clients;
	}
}
